# moqui_example

A comprehensive Moqui Framework example application with business components for E-commerce, Project Management, Manufacturing, and more.

## Welcome to Moqui Framework

[![license](https://img.shields.io/badge/license-CC0%201.0%20Universal-blue.svg)](https://github.com/moqui/moqui-framework/blob/master/LICENSE.md)
[![build](https://travis-ci.org/moqui/moqui-framework.svg)](https://travis-ci.org/moqui/moqui-framework)
[![release](https://img.shields.io/github/release/moqui/moqui-framework.svg)](https://github.com/moqui/moqui-framework/releases)
[![commits since release](http://img.shields.io/github/commits-since/moqui/moqui-framework/v3.0.0.svg)](https://github.com/moqui/moqui-framework/commits/master)
[![downloads](https://img.shields.io/github/downloads/moqui/moqui-framework/total.svg)](https://github.com/moqui/moqui-framework/releases)
[![downloads](https://img.shields.io/github/downloads/moqui/moqui-framework/v3.0.0/total.svg)](https://github.com/moqui/moqui-framework/releases/tag/v3.0.0)

[![Discourse Forum](https://img.shields.io/badge/moqui%20forum-discourse-blue.svg)](https://forum.moqui.org)
[![Google Group](https://img.shields.io/badge/google%20group-moqui-blue.svg)](https://groups.google.com/d/forum/moqui)
[![LinkedIn Group](https://img.shields.io/badge/linked%20in%20group-moqui-blue.svg)](https://www.linkedin.com/groups/4640689)
[![Gitter Chat at https://gitter.im/moqui/moqui-framework](https://badges.gitter.im/moqui/moqui-framework.svg)](https://gitter.im/moqui/moqui-framework?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Stack Overflow](https://img.shields.io/badge/stack%20overflow-moqui-blue.svg)](http://stackoverflow.com/questions/tagged/moqui)


For information about community infrastructure for code, discussions, support, etc see the Community Guide:

<https://www.moqui.org/docs/moqui/Community+Guide>

For details about running and deploying Moqui see:

<https://www.moqui.org/docs/framework/Run+and+Deploy>

Note that a runtime directory is required for Moqui Framework to run, but is not included in the source repository. The
Gradle get component, load, and run tasks will automatically add the default runtime (from the moqui-runtime repository).

For information about the current and near future status of Moqui Framework
see the [ReleaseNotes.md](https://github.com/moqui/moqui-framework/blob/master/ReleaseNotes.md) file.

For an overview of features see:

<https://www.moqui.org/docs/framework/Framework+Features>

Get started with Moqui development quickly using the Tutorial at:

<https://www.moqui.org/docs/framework/Quick+Tutorial>

For comprehensive documentation of Moqui Framework see the wiki based documentation on moqui.org (*running on Moqui HiveMind*):
 
For comprehensive documentation of Moqui Framework see the wiki based documentation on moqui.org (*running on Moqui HiveMind*):
 
<https://www.moqui.org/m/docs/framework>

---

## Project Overview

This repository is a comprehensive Moqui Framework application with production-ready business components including:

- **E-Commerce** (PopCommerce) - Order management, products, pricing, inventory
- **Project Management** (HiveMind) - Tasks, projects, collaboration, wikis
- **Manufacturing** (MarbleERP) - Production planning, work orders, BOM management
- **Master Data** (mantle-udm) - Party management, organizations, core entities
- **Reusable Services** (mantle-usl) - Common business logic patterns
- **UI Components** (SimpleScreens) - Dashboards, screens, reporting

## Getting Started

### Prerequisites
- Java 11 or higher
- Gradle 6.0+ (included via wrapper)
- PostgreSQL 12+ (recommended) or MySQL 5.7+
- Docker & Docker Compose (optional, for containerized deployment)

### Quick Start

#### 1. Clone and Setup
```bash
git clone https://github.com/louisburroughs/moqui_example.git
cd moqui_example
```

#### 2. Build Application
```bash
# Development build (skip tests for faster iteration)
./gradlew build -x test

# Full build with tests
./gradlew build
```

#### 3. Run Locally with Docker Compose
```bash
# Create development environment file (NEVER commit this!)
cat > .env.local << EOF
DB_USER=moqui_user
DB_PASSWORD=dev_password
ADMIN_PASSWORD=admin_password
EOF

chmod 600 .env.local

# Start all services (PostgreSQL, Moqui, Elasticsearch, Nginx)
docker-compose -f docker/moqui-postgres-compose.yml up -d

# Check logs
docker-compose -f docker/moqui-postgres-compose.yml logs -f moqui

# Access application
open http://localhost:8080/webroot/
```

#### 4. Stop Services
```bash
docker-compose -f docker/moqui-postgres-compose.yml down

# Remove data (clean slate)
docker-compose -f docker/moqui-postgres-compose.yml down -v
```

### Alternative: Run Without Docker

```bash
# Build and create runtime
./gradlew build

# Start Moqui (using embedded H2 database for development)
java -jar runtime/build/libs/moqui.war

# Access at http://localhost:8080/webroot/
```

## Project Structure

```
.
├── framework/                      # Moqui Framework core
│   ├── entity/                    # Core entity definitions
│   ├── service/                   # Core service implementations
│   ├── screen/                    # Core screens
│   └── src/                       # Java/Groovy source code
├── runtime/
│   ├── component/                 # Business components
│   │   ├── PopCommerce/          # E-commerce domain
│   │   ├── HiveMind/             # Project management
│   │   ├── MarbleERP/            # Manufacturing
│   │   ├── SimpleScreens/        # UI components
│   │   ├── mantle-udm/           # Universal data model
│   │   ├── mantle-usl/           # Reusable services
│   │   └── ...
│   ├── conf/                      # Configuration
│   ├── db/                        # Database scripts
│   └── lib/                       # Libraries
├── docker/                        # Docker build & compose files
│   ├── moqui-postgres-compose.yml # PostgreSQL dev environment
│   ├── moqui-mysql-compose.yml    # MySQL alternative
│   ├── simple/                    # Simple Dockerfile
│   └── ...
├── .github/
│   ├── agents/                    # AI agent guides
│   │   ├── api-agent.md          # REST API development
│   │   ├── architecture-agent.md # System architecture
│   │   └── dev-deploy-agent.md   # Deployment & DevOps
│   └── adr/                       # Architecture decision records
├── gradle/                        # Gradle wrapper
├── build.gradle                   # Root build configuration
└── MoquiInit.properties          # Application initialization
```

## Development

### Architecture & Design

This project follows **Domain-Driven Design (DDD)** principles:

- **Bounded Contexts** - Each domain owns its entities, services, and screens
- **Layering** - UI → Services → Entities (strict separation)
- **Integration Pattern** - External systems via Positivity layer
- **Service Naming** - Consistent: `{domain}.{service-type}#{Action}`

See [Architecture Guide](.github/agents/architecture-agent.md) for detailed design principles.

### REST API Development

All REST endpoints follow consistent patterns:

- **Endpoint Structure** - `/rest/api/v{version}/{resource}/{id}/{action}`
- **Error Handling** - Standardized error codes and messages
- **Validation** - Input validation at service layer
- **Authorization** - Role-based access control

See [API Agent Guide](.github/agents/api-agent.md) for REST API design patterns.

### Database

#### PostgreSQL (Recommended)
```bash
# Start PostgreSQL service
docker-compose -f docker/postgres-compose.yml up -d

# Connect to database
docker exec -it moqui-dev-postgres psql -U moqui_user -d moqui
```

#### MySQL
```bash
# Alternative MySQL setup
docker-compose -f docker/moqui-mysql-compose.yml up -d
```

### Testing

```bash
# Run all tests
./gradlew test

# Run specific component tests
./gradlew :runtime:component:PopCommerce:test

# Run with coverage
./gradlew test --profile
```

### Building & Deployment

#### Development Build
```bash
./gradlew build -x test  # Skip tests for faster builds
```

#### Docker Image
```bash
# Build development image
docker build -t moqui-dev:latest -f docker/simple/Dockerfile .

# Run container
docker run -p 8080:8080 moqui-dev:latest
```

See [Dev-Deploy Agent](.github/agents/dev-deploy-agent.md) for comprehensive deployment guidance.

## Documentation

### Core Documentation
- **[Moqui Framework Docs](https://www.moqui.org/docs/framework)** - Official framework documentation
- **[Community Guide](https://www.moqui.org/docs/moqui/Community+Guide)** - Community resources
- **[Quick Tutorial](https://www.moqui.org/docs/framework/Quick+Tutorial)** - Getting started tutorial

### Project-Specific Guides
- **[Architecture Agent](.github/agents/architecture-agent.md)** - System design & domain boundaries
- **[API Agent](.github/agents/api-agent.md)** - REST endpoint design patterns
- **[Dev-Deploy Agent](.github/agents/dev-deploy-agent.md)** - Build, test, and deployment
- **[Architecture Decision Records](.github/adr/)** - Major architectural decisions

### Components
- **PopCommerce** - E-commerce order and product management
- **HiveMind** - Project management and collaboration platform
- **SimpleScreens** - Reusable UI components and screens
- **MarbleERP** - Manufacturing and resource planning
- **mantle-udm** - Universal Data Model (core entities)
- **mantle-usl** - Universal Service Library (reusable patterns)

## AI Agents

This repository includes specialized AI agents to guide development:

### Architecture Agent
Reviews architectural decisions and enforces domain boundaries.
- Prevents architectural drift
- Validates layering rules
- Manages cross-domain dependencies

**Usage:** Consult for system design, feature decomposition, and architectural reviews.

### API Agent
Guides REST API development with consistent patterns.
- Designs endpoints following REST standards
- Implements comprehensive error handling
- Creates API documentation and tests

**Usage:** Create new REST endpoints, design error handlers, document API contracts.

### Dev-Deploy Agent
Manages builds, containerization, and local deployment.
- Orchestrates Docker builds and deployments
- Secures secrets and credentials
- Optimizes build performance

**Usage:** Build application, deploy to local Docker environment, troubleshoot deployments.

## Troubleshooting

### Application Won't Start
```bash
# Check logs
docker logs moqui-dev-app

# Verify database connectivity
docker-compose exec moqui psql -h postgres -U moqui_user -d moqui -c "SELECT 1"

# Rebuild without cache
docker-compose down
docker-compose build --no-cache
docker-compose up
```

### Build Failures
```bash
# Clean and rebuild
./gradlew clean build

# Check Java version
java -version  # Should be 11+

# Check disk space
df -h
```

### Database Issues
```bash
# Start fresh database
docker-compose down -v
docker-compose up -d postgres

# Initialize schema
docker-compose exec moqui gradle load
```

## Contributing

1. **Check Architecture** - Consult architecture-agent.md for design guidance
2. **Create Feature Branch** - `git checkout -b feature/your-feature`
3. **Develop & Test** - Write code and tests
4. **Architecture Review** - Get approval from Architecture Agent
5. **Submit PR** - Include architecture review comments
6. **Merge** - After all checks pass

## Community & Support

### Official Resources
- **Forum** - [forum.moqui.org](https://forum.moqui.org)
- **Google Group** - [Moqui Google Group](https://groups.google.com/d/forum/moqui)
- **Stack Overflow** - Tag questions with `[moqui]`
- **Gitter Chat** - [Moqui Gitter](https://gitter.im/moqui/moqui-framework)

### This Project
- **Issues** - [GitHub Issues](https://github.com/louisburroughs/moqui_example/issues)
- **Discussions** - GitHub Discussions (if enabled)

## License

See [LICENSE.md](LICENSE.md) for details. Moqui Framework is under CC0 1.0 Universal (Public Domain).

## Quick Reference

### Common Commands

```bash
# Build
./gradlew build                          # Full build with tests
./gradlew build -x test                  # Skip tests
./gradlew :runtime:component:PopCommerce:build  # Component build

# Test
./gradlew test                           # Run all tests
./gradlew test --tests "OrderTest"       # Run specific test

# Docker
docker-compose up -d                     # Start services
docker-compose down                      # Stop services
docker-compose logs -f                   # Follow logs

# Database
docker exec -it moqui-dev-postgres psql -U moqui_user -d moqui

# Development
./gradlew build -x test --continuous     # Watch mode (rebuild on changes)
```

### Default URLs
- **Application** - http://localhost:8080/webroot/
- **Database** - localhost:5432 (PostgreSQL)
- **Elasticsearch** - http://localhost:9200 (if running)

---

For more detailed information, see the [Moqui Framework documentation](https://www.moqui.org/docs/framework) and this project's [architecture guides](.github/agents/).
