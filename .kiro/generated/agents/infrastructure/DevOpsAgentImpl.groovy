package agents.infrastructure

import agents.interfaces.Agent
import agents.interfaces.AgentRequest
import agents.interfaces.AgentResponse
import groovy.transform.CompileStatic

/**
 * DevOps Agent Implementation
 * 
 * Requirements: REQ-006 (DevOps and Deployment Support)
 * - AC1: Deployment guidance (3 seconds, 95% accuracy)
 * - AC2: Container guidance (4 seconds, 97% accuracy)
 * - AC3: Monitoring guidance (2 seconds, 98% accuracy)
 * - AC4: Scaling guidance (5 seconds, 100% accuracy)
 * - AC5: Troubleshooting guidance (4 seconds, 90% accuracy)
 */
@CompileStatic
class DevOpsAgentImpl implements Agent {
    
    private static final String AGENT_ID = "devops-agent"
    private static final String AGENT_NAME = "DevOps Agent"
    
    @Override
    String getAgentId() { return AGENT_ID }
    
    @Override
    String getAgentName() { return AGENT_NAME }
    
    @Override
    List<String> getCapabilities() {
        return [
            "deployment-configuration",
            "environment-configuration",
            "clustering-loadbalancing",
            "container-orchestration",
            "monitoring-setup",
            "performance-tracking",
            "troubleshooting",
            "scaling-guidance"
        ]
    }
    
    @Override
    AgentResponse processRequest(AgentRequest request) {
        long startTime = System.currentTimeMillis()
        
        try {
            String guidance = switch (request.requestType) {
                case "deployment-configuration" -> provideDeploymentGuidance(request)
                case "environment-configuration" -> provideEnvironmentGuidance(request)
                case "clustering-loadbalancing" -> provideClusteringGuidance(request)
                case "container-orchestration" -> provideContainerGuidance(request)
                case "monitoring-setup" -> provideMonitoringGuidance(request)
                case "performance-tracking" -> providePerformanceTrackingGuidance(request)
                case "troubleshooting" -> provideTroubleshootingGuidance(request)
                case "scaling-guidance" -> provideScalingGuidance(request)
                default -> "Unknown request type: ${request.requestType}"
            }
            
            long responseTime = System.currentTimeMillis() - startTime
            
            return new AgentResponse(
                agentId: AGENT_ID,
                requestId: request.requestId,
                guidance: guidance,
                responseTimeMs: responseTime,
                success: true
            )
        } catch (Exception e) {
            return new AgentResponse(
                agentId: AGENT_ID,
                requestId: request.requestId,
                guidance: "Error processing request: ${e.message}",
                responseTimeMs: System.currentTimeMillis() - startTime,
                success: false,
                errorMessage: e.message
            )
        }
    }
    
    @Override
    boolean canHandle(AgentRequest request) {
        return getCapabilities().contains(request.requestType)
    }
    
    @Override
    Map<String, Object> getHealth() {
        return [
            status: "healthy",
            agentId: AGENT_ID,
            capabilities: getCapabilities().size(),
            timestamp: System.currentTimeMillis()
        ]
    }
    
    // AC1: Deployment configuration guidance (3 seconds, 95% accuracy)
    private String provideDeploymentGuidance(AgentRequest request) {
        String component = request.context?.component ?: "unknown"
        String environment = request.context?.environment ?: "production"
        
        return """
# Moqui Deployment Configuration for ${component}

## Environment: ${environment}

### 1. Configuration File Structure
- **Location**: runtime/conf/MoquiProductionConf.xml (or MoquiDevConf.xml)
- **Override**: Use environment variables or system properties

### 2. Database Configuration
```xml
<entity-facade>
    <datasource group-name="transactional">
        <inline-jdbc jdbc-uri="jdbc:postgresql://localhost:5432/moqui"
                     jdbc-username="\${db_user}" jdbc-password="\${db_password}"
                     pool-minsize="5" pool-maxsize="50"/>
    </datasource>
</entity-facade>
```

### 3. Server Configuration
```xml
<webapp-list>
    <webapp name="webroot" http-port="8080" https-port="8443"
            https-enabled="true" http-host="0.0.0.0"/>
</webapp-list>
```

### 4. Deployment Steps
1. Build: `./gradlew build -x test`
2. Package: Create runtime directory with all components
3. Configure: Set environment-specific MoquiConf.xml
4. Deploy: Copy runtime to target server
5. Start: `java -jar moqui.war` or use systemd service

### 5. Environment Variables
- DB_USER, DB_PASSWORD: Database credentials
- MOQUI_CONF: Path to configuration file
- JAVA_OPTS: JVM options (-Xmx, -Xms, etc.)

### 6. Health Check Endpoint
- URL: /rest/s1/moqui/health
- Expected: 200 OK with JSON status
"""
    }
    
    // AC1: Environment-specific configuration patterns
    private String provideEnvironmentGuidance(AgentRequest request) {
        String environment = request.context?.environment ?: "production"
        
        return """
# Environment-Specific Configuration: ${environment}

## Configuration Hierarchy
1. MoquiDefaultConf.xml (framework defaults)
2. MoquiConf.xml (component-specific)
3. Moqui${environment.capitalize()}Conf.xml (environment-specific)
4. Environment variables (highest priority)

## ${environment.capitalize()} Configuration

### Database Settings
${environment == "production" ? """
- Connection pool: 50-100 connections
- Timeout: 30 seconds
- Validation query: SELECT 1
- SSL mode: require
""" : """
- Connection pool: 5-10 connections
- Timeout: 10 seconds
- Validation query: SELECT 1
- SSL mode: prefer
"""}

### Logging Configuration
```xml
<screen-facade>
    <screen-text-output type="text/html" 
                        compress="true" 
                        cache-pages="${environment == 'production' ? 'true' : 'false'}"/>
</screen-facade>
```

### Cache Configuration
${environment == "production" ? """
- Entity cache: Enabled (1000 entries per entity)
- Screen cache: Enabled (500 screens)
- Service cache: Enabled (200 services)
""" : """
- Entity cache: Minimal (100 entries per entity)
- Screen cache: Disabled
- Service cache: Disabled
"""}

### Security Settings
- HTTPS: ${environment == "production" ? "Required" : "Optional"}
- Session timeout: ${environment == "production" ? "30 minutes" : "60 minutes"}
- CORS: ${environment == "production" ? "Restricted" : "Permissive"}
"""
    }
    
    // AC4: Clustering and load balancing guidance (5 seconds, 100% accuracy)
    private String provideClusteringGuidance(AgentRequest request) {
        return """
# Moqui Clustering and Load Balancing

## 1. Clustering Configuration

### Session Replication
```xml
<webapp-list>
    <webapp name="webroot" session-timeout="30">
        <session-config>
            <cookie-config http-only="true" secure="true"/>
            <tracking-mode>COOKIE</tracking-mode>
        </session-config>
    </webapp>
</webapp-list>
```

### Distributed Cache (Hazelcast)
```xml
<cache-facade>
    <cache-provider class="org.moqui.impl.cache.HazelcastCacheProvider">
        <property name="cluster-name" value="moqui-cluster"/>
        <property name="cluster-password" value="\${cluster_password}"/>
    </cache-provider>
</cache-facade>
```

## 2. Load Balancer Configuration

### Nginx Configuration
```nginx
upstream moqui_backend {
    least_conn;
    server moqui1:8080 max_fails=3 fail_timeout=30s;
    server moqui2:8080 max_fails=3 fail_timeout=30s;
    server moqui3:8080 max_fails=3 fail_timeout=30s;
}

server {
    listen 80;
    server_name example.com;
    
    location / {
        proxy_pass http://moqui_backend;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
    
    # Health check endpoint
    location /health {
        proxy_pass http://moqui_backend/rest/s1/moqui/health;
        access_log off;
    }
}
```

## 3. Database Clustering
- Use PostgreSQL replication (primary-replica)
- Configure read replicas for reporting queries
- Use connection pooling with failover

## 4. File Storage Clustering
- Use shared NFS or S3-compatible storage
- Configure runtime/db and runtime/sessions on shared storage
- Avoid local file storage for user uploads

## 5. Monitoring Cluster Health
- Monitor each node's /rest/s1/moqui/health endpoint
- Track session distribution across nodes
- Monitor cache hit rates and replication lag
"""
    }
    
    // AC2: Container orchestration guidance (4 seconds, 97% accuracy)
    private String provideContainerGuidance(AgentRequest request) {
        return """
# Moqui Container Orchestration

## 1. Docker Configuration

### Dockerfile
```dockerfile
FROM eclipse-temurin:11-jre
WORKDIR /opt/moqui
COPY runtime ./runtime
COPY moqui.war ./
EXPOSE 8080 8443
ENV JAVA_OPTS="-Xmx2g -Xms512m"
CMD ["java", "-jar", "moqui.war"]
```

### Docker Compose
```yaml
version: '3.8'
services:
  moqui:
    image: moqui:latest
    ports:
      - "8080:8080"
    environment:
      - DB_USER=\${DB_USER}
      - DB_PASSWORD=\${DB_PASSWORD}
      - MOQUI_CONF=conf/MoquiProductionConf.xml
    volumes:
      - moqui-runtime:/opt/moqui/runtime
    depends_on:
      - postgres
  
  postgres:
    image: postgres:14
    environment:
      - POSTGRES_DB=moqui
      - POSTGRES_USER=\${DB_USER}
      - POSTGRES_PASSWORD=\${DB_PASSWORD}
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  moqui-runtime:
  postgres-data:
```

## 2. Kubernetes Deployment

### Deployment YAML
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: moqui
spec:
  replicas: 3
  selector:
    matchLabels:
      app: moqui
  template:
    metadata:
      labels:
        app: moqui
    spec:
      containers:
      - name: moqui
        image: moqui:latest
        ports:
        - containerPort: 8080
        env:
        - name: DB_USER
          valueFrom:
            secretKeyRef:
              name: moqui-secrets
              key: db-user
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: moqui-secrets
              key: db-password
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /rest/s1/moqui/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /rest/s1/moqui/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 5
```

### Service YAML
```yaml
apiVersion: v1
kind: Service
metadata:
  name: moqui-service
spec:
  selector:
    app: moqui
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

## 3. Container Best Practices
- Use multi-stage builds to minimize image size
- Store secrets in Kubernetes Secrets or Docker Secrets
- Use health checks for automatic recovery
- Configure resource limits to prevent resource exhaustion
- Use persistent volumes for runtime/db and runtime/sessions
"""
    }
    
    // AC3: Monitoring guidance (2 seconds, 98% accuracy)
    private String provideMonitoringGuidance(AgentRequest request) {
        return """
# Moqui Monitoring Setup

## 1. Health Check Endpoint
- **URL**: /rest/s1/moqui/health
- **Response**: JSON with system status
- **Frequency**: Every 30 seconds

## 2. JMX Monitoring
```bash
# Enable JMX in JAVA_OPTS
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9010
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
```

## 3. Application Metrics
- Request count and response times
- Database connection pool usage
- Cache hit/miss rates
- Session count and duration
- Error rates by endpoint

## 4. Infrastructure Metrics
- CPU usage per container/node
- Memory usage and GC activity
- Disk I/O and space usage
- Network throughput

## 5. Logging Configuration
```xml
<screen-facade>
    <screen-output type="text/html">
        <log-level name="org.moqui" level="INFO"/>
        <log-level name="org.moqui.impl.service" level="DEBUG"/>
    </screen-output>
</screen-facade>
```

## 6. Alerting Rules
- Response time > 2 seconds: Warning
- Error rate > 5%: Critical
- Database connection pool > 80%: Warning
- Memory usage > 85%: Warning
- Disk space < 10%: Critical
"""
    }
    
    // AC3: Performance tracking guidance
    private String providePerformanceTrackingGuidance(AgentRequest request) {
        return """
# Moqui Performance Tracking

## 1. Built-in Performance Monitoring
- Enable service call timing in MoquiConf.xml
- Track entity query performance
- Monitor screen render times

## 2. Custom Metrics
```groovy
// In service implementation
long startTime = System.currentTimeMillis()
// ... service logic ...
long duration = System.currentTimeMillis() - startTime
ec.logger.info("Service execution time: \${duration}ms")
```

## 3. Database Query Performance
- Enable slow query logging in PostgreSQL
- Monitor query execution plans
- Track connection pool metrics

## 4. Performance Baselines
- P50 response time: < 500ms
- P95 response time: < 2000ms
- P99 response time: < 5000ms
- Database query time: < 100ms (P95)
"""
    }
    
    // AC5: Troubleshooting guidance (4 seconds, 90% accuracy)
    private String provideTroubleshootingGuidance(AgentRequest request) {
        String issue = request.context?.issue ?: "general"
        
        return """
# Moqui Troubleshooting Guide: ${issue}

## Common Issues and Solutions

### 1. Application Won't Start
- Check Java version: `java -version` (must be 11+)
- Verify database connectivity
- Check logs: runtime/log/moqui.log
- Validate MoquiConf.xml syntax

### 2. Database Connection Errors
- Verify database is running
- Check credentials in environment variables
- Test connection: `psql -h localhost -U moqui_user -d moqui`
- Check connection pool settings

### 3. Performance Issues
- Check database query performance
- Monitor JVM memory usage
- Review cache configuration
- Check for N+1 query problems

### 4. Memory Leaks
- Enable heap dump on OutOfMemoryError
- Use JVisualVM or JProfiler
- Check for unclosed database connections
- Review cache size limits

### 5. Session Issues
- Verify session timeout configuration
- Check session storage (database or file)
- Monitor session count
- Review cookie configuration

## Debugging Tools
- Logs: runtime/log/moqui.log
- JMX: Connect with JConsole or VisualVM
- Database: Check pg_stat_activity for active queries
- Health endpoint: /rest/s1/moqui/health

## Emergency Procedures
1. Restart application: `systemctl restart moqui`
2. Clear cache: Delete runtime/db/h2 or runtime/sessions
3. Database rollback: Use transaction logs
4. Failover: Switch to backup instance
"""
    }
    
    // AC4: Scaling guidance (5 seconds, 100% accuracy)
    private String provideScalingGuidance(AgentRequest request) {
        return """
# Moqui Scaling Guidance

## 1. Horizontal Scaling (Recommended)
- Add more Moqui instances behind load balancer
- Use shared database and file storage
- Configure distributed cache (Hazelcast)
- Implement session replication

### Scaling Strategy
- Start with 2-3 instances for redundancy
- Add instances based on CPU/memory usage
- Target 60-70% resource utilization per instance
- Use auto-scaling based on request rate

## 2. Vertical Scaling
- Increase JVM heap size: -Xmx4g -Xms1g
- Add more CPU cores (Moqui is multi-threaded)
- Increase database connection pool size
- Optimize database with more RAM

## 3. Database Scaling
- Use read replicas for reporting queries
- Implement connection pooling (50-100 connections)
- Partition large tables by date or tenant
- Use database caching (Redis/Memcached)

## 4. Caching Strategy
- Enable entity cache for frequently accessed data
- Use screen cache for static content
- Implement CDN for static assets
- Configure cache expiration policies

## 5. Performance Optimization
- Optimize database queries and indexes
- Use view-entities for complex queries
- Implement lazy loading for large datasets
- Compress HTTP responses

## 6. Monitoring for Scaling Decisions
- CPU usage > 70%: Add instances
- Memory usage > 80%: Increase heap or add instances
- Database connections > 80%: Scale database or add read replicas
- Response time > 2s: Investigate bottlenecks

## 7. Auto-Scaling Configuration (Kubernetes)
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: moqui-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: moqui
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
```
"""
    }
}
