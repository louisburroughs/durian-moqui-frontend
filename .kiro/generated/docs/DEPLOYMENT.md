# Agent Structure Deployment Guide

## Overview

This guide provides step-by-step procedures for deploying the Durion Moqui Frontend agent structure system in development, staging, and production environments.

## Prerequisites

- Docker 20.10+
- Docker Compose 2.0+
- Java 11+
- Gradle 7.0+
- PostgreSQL 12+ (for production)
- nginx (for load balancing)

## Deployment Architectures

### Development Environment

**Single-instance deployment** for local development:

```yaml
# docker-compose-dev.yml
version: '3.8'
services:
  agent-service:
    build: .
    ports:
      - "8080:8080"
    environment:
      - ENVIRONMENT=development
      - LOG_LEVEL=DEBUG
    volumes:
      - ./.kiro/generated:/app/.kiro/generated
```

**Commands**:
```bash
# Start development environment
docker-compose -f docker-compose-dev.yml up -d

# View logs
docker-compose -f docker-compose-dev.yml logs -f

# Stop environment
docker-compose -f docker-compose-dev.yml down
```

### Staging Environment

**Multi-instance deployment** with load balancing:

```yaml
# docker-compose-staging.yml
version: '3.8'
services:
  agent-primary:
    build: .
    environment:
      - ENVIRONMENT=staging
      - INSTANCE_ROLE=primary
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
      interval: 5s
      timeout: 3s
      retries: 3

  agent-secondary:
    build: .
    environment:
      - ENVIRONMENT=staging
      - INSTANCE_ROLE=secondary
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
      interval: 5s
      timeout: 3s
      retries: 3

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./.kiro/generated/config/nginx-ha.conf:/etc/nginx/nginx.conf
    depends_on:
      - agent-primary
      - agent-secondary
```

**Commands**:
```bash
# Start staging environment
docker-compose -f docker-compose-staging.yml up -d

# Check health
curl http://localhost/health

# View load balancer status
docker-compose exec nginx nginx -T

# Stop environment
docker-compose -f docker-compose-staging.yml down
```

### Production Environment

**High-availability deployment** with automatic failover:

Use `.kiro/generated/config/ha-deployment.yml` (created in Task 8.2):

```bash
# Start production environment
docker-compose -f .kiro/generated/config/ha-deployment.yml up -d

# Verify all services are running
docker-compose -f .kiro/generated/config/ha-deployment.yml ps

# Check agent health
curl http://localhost/health

# View backup status
docker-compose -f .kiro/generated/config/ha-deployment.yml logs backup

# Stop environment (graceful shutdown)
docker-compose -f .kiro/generated/config/ha-deployment.yml down
```

## Deployment Steps

### Step 1: Build Agent System

```bash
# Clean previous builds
./gradlew clean

# Build agent system
./gradlew build

# Verify build artifacts
ls -la .kiro/generated/agents/
```

### Step 2: Configure Environment

Create environment-specific configuration:

**Development** (`.env.development`):
```bash
ENVIRONMENT=development
LOG_LEVEL=DEBUG
AGENT_POOL_SIZE=2
HEALTH_CHECK_INTERVAL=10
BACKUP_ENABLED=false
```

**Staging** (`.env.staging`):
```bash
ENVIRONMENT=staging
LOG_LEVEL=INFO
AGENT_POOL_SIZE=5
HEALTH_CHECK_INTERVAL=5
BACKUP_ENABLED=true
BACKUP_SCHEDULE="0 */4 * * *"
```

**Production** (`.env.production`):
```bash
ENVIRONMENT=production
LOG_LEVEL=WARN
AGENT_POOL_SIZE=10
HEALTH_CHECK_INTERVAL=5
BACKUP_ENABLED=true
BACKUP_SCHEDULE="0 */4 * * *"
FAILOVER_TIMEOUT=30
DEGRADED_MODE_THRESHOLD=0.8
```

### Step 3: Initialize Database

```bash
# Create database schema
./gradlew initDatabase

# Load seed data
./gradlew loadSeedData

# Verify database
./gradlew verifyDatabase
```

### Step 4: Deploy Agent Services

**Development**:
```bash
docker-compose -f docker-compose-dev.yml up -d
```

**Staging**:
```bash
docker-compose -f docker-compose-staging.yml up -d
```

**Production**:
```bash
docker-compose -f .kiro/generated/config/ha-deployment.yml up -d
```

### Step 5: Verify Deployment

```bash
# Check service health
curl http://localhost/health

# Expected response:
# {
#   "status": "HEALTHY",
#   "agents": {
#     "moqui-framework-agent": "HEALTHY",
#     "domain-agent": "HEALTHY",
#     "security-agent": "HEALTHY",
#     ...
#   },
#   "uptime": 3600,
#   "version": "1.0.0"
# }

# Test agent request
curl -X POST http://localhost/api/agents/request \
  -H "Content-Type: application/json" \
  -d '{
    "agentId": "moqui-framework-agent",
    "capability": "entity-guidance",
    "parameters": {
      "entityName": "Customer"
    }
  }'

# Verify response time < 2 seconds
```

### Step 6: Configure Monitoring

```bash
# Enable performance monitoring
docker-compose exec agent-primary /app/scripts/enable-monitoring.sh

# View metrics
curl http://localhost/metrics

# Expected metrics:
# - agent_response_time_p50
# - agent_response_time_p95
# - agent_response_time_p99
# - agent_request_count
# - agent_error_rate
# - agent_health_status
```

### Step 7: Configure Backup (Production Only)

```bash
# Verify backup service is running
docker-compose -f .kiro/generated/config/ha-deployment.yml ps backup

# Trigger manual backup
docker-compose exec backup /app/scripts/backup-now.sh

# Verify backup
ls -la /backups/agent-structure/

# Expected files:
# - agent-state-YYYYMMDD-HHMMSS.tar.gz
# - agent-state-YYYYMMDD-HHMMSS.checksum
```

## Rollback Procedures

### Rollback to Previous Version

```bash
# Stop current deployment
docker-compose down

# Restore previous version
git checkout <previous-version-tag>

# Rebuild
./gradlew clean build

# Redeploy
docker-compose up -d

# Verify rollback
curl http://localhost/health
```

### Restore from Backup

```bash
# Stop agent services
docker-compose down

# List available backups
ls -la /backups/agent-structure/

# Restore from backup
docker-compose exec backup /app/scripts/restore-backup.sh \
  /backups/agent-structure/agent-state-20250101-120000.tar.gz

# Verify checksum
docker-compose exec backup /app/scripts/verify-checksum.sh \
  /backups/agent-structure/agent-state-20250101-120000.tar.gz

# Restart services
docker-compose up -d

# Verify restoration
curl http://localhost/health
```

## Scaling Procedures

### Horizontal Scaling

Add more agent instances:

```yaml
# docker-compose-scale.yml
services:
  agent-instance-1:
    build: .
    environment:
      - INSTANCE_ID=1
  
  agent-instance-2:
    build: .
    environment:
      - INSTANCE_ID=2
  
  agent-instance-3:
    build: .
    environment:
      - INSTANCE_ID=3
```

```bash
# Scale to 3 instances
docker-compose -f docker-compose-scale.yml up -d --scale agent-instance=3

# Verify load distribution
curl http://localhost/metrics | grep agent_request_count
```

### Vertical Scaling

Increase resources per instance:

```yaml
services:
  agent-service:
    deploy:
      resources:
        limits:
          cpus: '2.0'
          memory: 4G
        reservations:
          cpus: '1.0'
          memory: 2G
```

## Monitoring and Alerting

### Health Checks

```bash
# Agent health endpoint
curl http://localhost/health

# Individual agent health
curl http://localhost/health/moqui-framework-agent

# System health
curl http://localhost/health/system
```

### Performance Metrics

```bash
# Response time metrics
curl http://localhost/metrics/response-time

# Throughput metrics
curl http://localhost/metrics/throughput

# Error rate metrics
curl http://localhost/metrics/error-rate
```

### Alerting Configuration

Configure alerts in `.kiro/generated/config/alerts.yml`:

```yaml
alerts:
  - name: high-response-time
    condition: agent_response_time_p95 > 5000
    severity: warning
    action: notify-team
  
  - name: high-error-rate
    condition: agent_error_rate > 0.05
    severity: critical
    action: trigger-failover
  
  - name: agent-unhealthy
    condition: agent_health_status != "HEALTHY"
    severity: critical
    action: restart-agent
```

## Disaster Recovery

### Automatic Failover

Configured in `.kiro/generated/agents/core/DisasterRecovery.groovy`:

- **Failover Time**: 30 seconds (REQ-010 AC2)
- **Data Consistency**: 100% guaranteed (REQ-010 AC3)
- **Functionality Retention**: 80% in degraded mode (REQ-010 AC4)

**Test Failover**:
```bash
# Simulate primary failure
docker-compose stop agent-primary

# Verify automatic failover (within 30 seconds)
curl http://localhost/health

# Expected: secondary promoted to primary
# {
#   "status": "DEGRADED",
#   "primary": "agent-secondary",
#   "failover_time": 28,
#   "functionality": 0.82
# }
```

### Manual Recovery

```bash
# Restore primary from backup
docker-compose exec backup /app/scripts/restore-backup.sh \
  /backups/agent-structure/latest.tar.gz

# Restart primary
docker-compose restart agent-primary

# Verify recovery
curl http://localhost/health

# Expected: primary restored
# {
#   "status": "HEALTHY",
#   "primary": "agent-primary",
#   "recovery_time": 120
# }
```

## Security Considerations

### TLS Configuration

```bash
# Generate TLS certificates
./scripts/generate-certs.sh

# Configure nginx with TLS
# Edit .kiro/generated/config/nginx-ha.conf:
server {
    listen 443 ssl;
    ssl_certificate /etc/nginx/certs/server.crt;
    ssl_certificate_key /etc/nginx/certs/server.key;
    ssl_protocols TLSv1.3;
}
```

### Authentication

```bash
# Configure JWT authentication
export JWT_SECRET=$(openssl rand -base64 32)
export JWT_EXPIRY=3600

# Update environment configuration
echo "JWT_SECRET=${JWT_SECRET}" >> .env.production
echo "JWT_EXPIRY=${JWT_EXPIRY}" >> .env.production
```

### Network Security

```yaml
# docker-compose-secure.yml
services:
  agent-service:
    networks:
      - internal
    ports:
      - "8080"  # Internal only
  
  nginx:
    networks:
      - internal
      - external
    ports:
      - "443:443"  # HTTPS only

networks:
  internal:
    internal: true
  external:
```

## Troubleshooting

### Common Issues

**Issue**: Agent not responding
```bash
# Check agent health
docker-compose logs agent-primary | grep ERROR

# Restart agent
docker-compose restart agent-primary

# Verify recovery
curl http://localhost/health
```

**Issue**: High response time
```bash
# Check performance metrics
curl http://localhost/metrics/response-time

# Increase agent pool size
export AGENT_POOL_SIZE=20
docker-compose up -d

# Verify improvement
curl http://localhost/metrics/response-time
```

**Issue**: Backup failure
```bash
# Check backup logs
docker-compose logs backup | grep ERROR

# Verify disk space
df -h /backups

# Trigger manual backup
docker-compose exec backup /app/scripts/backup-now.sh
```

### Log Analysis

```bash
# View all logs
docker-compose logs -f

# Filter by service
docker-compose logs -f agent-primary

# Filter by level
docker-compose logs | grep ERROR

# Export logs
docker-compose logs > deployment-logs.txt
```

## Performance Tuning

### Agent Pool Configuration

```bash
# Increase pool size for high load
export AGENT_POOL_SIZE=20

# Adjust health check interval
export HEALTH_CHECK_INTERVAL=3

# Redeploy
docker-compose up -d
```

### Database Optimization

```bash
# Enable connection pooling
export DB_POOL_SIZE=50
export DB_POOL_TIMEOUT=30

# Configure caching
export CACHE_ENABLED=true
export CACHE_SIZE=1000

# Redeploy
docker-compose up -d
```

### Load Balancer Tuning

Edit `.kiro/generated/config/nginx-ha.conf`:

```nginx
upstream agent_backend {
    least_conn;
    server agent-primary:8080 max_fails=3 fail_timeout=30s;
    server agent-secondary:8080 max_fails=3 fail_timeout=30s backup;
    keepalive 32;
}
```

## Maintenance Procedures

### Scheduled Maintenance

```bash
# 1. Notify users
curl -X POST http://localhost/api/maintenance/notify \
  -d '{"message": "Scheduled maintenance in 1 hour"}'

# 2. Enable maintenance mode
curl -X POST http://localhost/api/maintenance/enable

# 3. Perform maintenance
./scripts/maintenance.sh

# 4. Disable maintenance mode
curl -X POST http://localhost/api/maintenance/disable

# 5. Verify system
curl http://localhost/health
```

### Database Maintenance

```bash
# Backup database
./gradlew backupDatabase

# Optimize database
./gradlew optimizeDatabase

# Verify database
./gradlew verifyDatabase
```

## Compliance and Auditing

### Audit Logging

```bash
# Enable audit logging
export AUDIT_ENABLED=true
export AUDIT_LOG_PATH=/var/log/agent-audit.log

# View audit logs
tail -f /var/log/agent-audit.log

# Export audit logs
./scripts/export-audit-logs.sh 2025-01-01 2025-12-31
```

### Compliance Checks

```bash
# Run compliance checks
./gradlew checkCompliance

# Generate compliance report
./gradlew generateComplianceReport

# View report
cat build/reports/compliance-report.html
```

## Deployment Checklist

### Pre-Deployment
- [ ] Build artifacts verified
- [ ] Environment configuration validated
- [ ] Database schema initialized
- [ ] TLS certificates configured
- [ ] Backup system tested
- [ ] Monitoring configured
- [ ] Alerting rules defined

### Deployment
- [ ] Services deployed
- [ ] Health checks passing
- [ ] Performance metrics within targets
- [ ] Load balancing verified
- [ ] Failover tested
- [ ] Backup verified

### Post-Deployment
- [ ] System health verified
- [ ] Performance targets met
- [ ] Security audit passed
- [ ] Documentation updated
- [ ] Team notified
- [ ] Monitoring active

## Support and Escalation

### Support Contacts
- **Development Team**: dev-team@durion.com
- **Operations Team**: ops-team@durion.com
- **Security Team**: security-team@durion.com

### Escalation Procedures
1. **Level 1**: Check logs and restart services
2. **Level 2**: Restore from backup
3. **Level 3**: Contact development team
4. **Level 4**: Initiate disaster recovery

## References

- Agent API Documentation: `.kiro/generated/docs/AGENT_API.md`
- Integration Guide: `.kiro/generated/docs/INTEGRATION_GUIDE.md`
- Troubleshooting Guide: `.kiro/generated/docs/TROUBLESHOOTING.md`
- Requirements: `.kiro/specs/agent-structure/requirements.md`
- Design: `.kiro/specs/agent-structure/design.md`
