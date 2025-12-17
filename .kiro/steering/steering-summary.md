# Durion Frontend Platform (durion-moqui-frontend) - Steering Summary

## Overview

This document consolidates all steering guidance for the Durion Frontend Platform (durion-moqui-frontend) system development. It provides a comprehensive reference for agents, developers, and stakeholders working on the Moqui Framework architecture.

## Product Overview

**Durion Frontend Platform** is a modern web application built on Moqui Framework for the Tire Industry Open Technology Foundation (TIOTF). It provides responsive web interfaces for ERP, e-commerce, project management, and manufacturing operations, integrating with the Positivity backend services.

### Core Business Domains

- **E-Commerce** - Product catalog, shopping cart, order processing, customer accounts
- **Project Management** - Task tracking, collaboration, wikis, time management
- **Manufacturing** - Production planning, work orders, BOM management, resource scheduling
- **Accounting** - Financial postings, GL, AR/AP, reconciliation
- **CRM** - Customer relationships, contacts, opportunities, service requests
- **Inventory** - Stock management, movements, locations, vehicle-specific inventory
- **Work Execution** - Shop operations, work orders, scheduling, technician assignment
- **Integration** - durion-positivity-backend services, vehicle reference data, external systems

## Technology Stack

### Backend Framework

- **Moqui Framework 3.x** - Enterprise application framework
- **Java 11** - Backend runtime (configured via .sdkmanrc)
- **Groovy** - Service implementation scripting language
- **Gradle** - Build system and dependency management
- **FreeMarker** - Server-side template engine for screens

### Frontend Framework

- **Vue.js 3.x** - Progressive JavaScript framework
- **Quasar v2.x** - Vue.js 3 component framework with Material Design
- **TypeScript 5.x** - Type-safe frontend development
- **Composition API** - Vue.js 3 component organization
- **ESLint + Prettier** - Code quality and formatting

### Data & Integration

- **PostgreSQL** - Primary production database
- **MySQL** - Alternative supported database
- **REST APIs** - Integration with durion-positivity-backend services (Spring Boot microservices)
- **mantle-udm** - Universal Data Model (entities)
- **mantle-usl** - Universal Service Library (business logic)

### Development & Deployment

- **Docker + Docker Compose** - Containerization and local development
- **npm/yarn** - Frontend dependency management
- **Spock Framework** - Backend BDD-style testing (Groovy specifications)
- **Jest** - Frontend testing (Vue.js/TypeScript components)
- **sdkman** - Java version management

## Project Structure

### Root Layout

```
durion-moqui-frontend/
├── framework/                       # Moqui Framework core
│   ├── entity/                     # Core entity definitions (XML)
│   ├── service/                    # Core service definitions (XML)
│   ├── screen/                     # Core screen definitions (XML)
│   └── src/                        # Java/Groovy source code
├── runtime/
│   ├── component/                  # Business components
│   │   ├── PopCommerce/           # E-commerce component
│   │   ├── HiveMind/              # Project management
│   │   ├── MarbleERP/             # Manufacturing
│   │   ├── SimpleScreens/         # UI components
│   │   ├── mantle-udm/            # Universal data model
│   │   ├── mantle-usl/            # Universal service library
│   │   ├── moqui-fop/             # FOP integration
│   │   ├── example/               # Example components
│   │   ├── durion-accounting/     # Durion accounting
│   │   ├── durion-common/         # Shared UI/utilities
│   │   ├── durion-crm/            # Durion CRM
│   │   ├── durion-demo-data/      # Demo datasets
│   │   ├── durion-experience/     # Frontend experience
│   │   ├── durion-inventory/      # Inventory management
│   │   ├── durion-positivity/     # Integration with durion-positivity-backend
│   │   ├── durion-product/        # Product catalogs
│   │   ├── durion-theme/          # Theme assets
│   │   └── durion-workexec/       # Work execution
│   ├── conf/                       # Configuration files
│   ├── db/                         # Database scripts
│   └── lib/                        # Runtime libraries
├── docker/                         # Docker compose files
├── .github/                        # Documentation, ADRs, instructions
├── .vscode/                        # VSCode workspace settings
├── build.gradle                    # Root Gradle build
└── settings.gradle                 # Gradle settings
```

### Moqui Component Structure

Each Moqui component follows this standard pattern:

- **entity/** - Entity definitions (XML) for data model
- **service/** - Service definitions (XML) with Groovy/Java implementations
- **screen/** - Screen definitions (XML) with FreeMarker templates
- **data/** - Seed and demo data (XML)
- **template/** - Email, report, and document templates
- **src/** - Java/Groovy source code for complex logic
- **webapp/** - Vue.js/TypeScript frontend components and assets

## Agent Integration Guide

### Core Development Agents

#### **moqui-architect** - System Architect

- **Purpose**: Component boundaries, domain design, system architecture
- **Use For**: Component decomposition, domain modeling, entity design, architectural decisions

#### **moqui-developer** - Primary Implementation Agent

- **Purpose**: Moqui Framework development and implementation
- **Pairing**: Works with **moqui-pair-navigator** for loop detection and quality
- **Use For**: Entity definitions, service implementations (Groovy), screen definitions, business logic

#### **moqui-pair-navigator** - Pairing & Quality Agent

- **Purpose**: Loop detection, architectural drift prevention, simplification guidance
- **Stop-Phrases**: Uses mandatory interruption phrases for problematic patterns
- **Use For**: Complex implementations, refactoring decisions, quality improvement

#### **vue-frontend-specialist** - Frontend Expert

- **Purpose**: Vue.js 3 + TypeScript frontend development
- **Use For**: Component development, Composition API, state management, TypeScript patterns

### Infrastructure & Operations Agents

#### **containerization-specialist** - DevOps Engineer

- **Purpose**: Docker containerization, orchestration, deployment
- **Use For**: Container optimization, Docker Compose for local development, deployment strategies

#### **moqui-database-specialist** - Database Expert

- **Purpose**: Entity modeling, database optimization, migration strategies
- **Use For**: Entity design, view-entity creation, database performance, PostgreSQL/MySQL optimization

#### **integration-specialist** - Integration Expert

- **Purpose**: REST API integration, external service connections
- **Use For**: durion-positivity-backend integration, durion-positivity component patterns, REST service implementation

### Quality & Security Agents

#### **moqui-testing-specialist** - Testing Expert

- **Purpose**: Moqui service and Vue.js component testing
- **Use For**: Spock Framework BDD tests, Jest component tests, integration testing

#### **security-specialist** - Security Expert

- **Purpose**: Moqui authentication, authorization, OWASP compliance
- **Use For**: User authentication, authorization rules, screen/service security, input validation

#### **frontend-testing-specialist** - Frontend Quality Expert

- **Purpose**: Vue.js component testing, E2E testing
- **Use For**: Jest unit tests, component testing, accessibility validation

## Development Instructions Integration

### Technology-Specific Instructions

#### **groovy.instructions.md**

- **Scope**: `**/*.groovy`, `**/*Service.groovy`, `**/*RestEvents.groovy`
- **Guidelines**: Groovy service implementations, Moqui Framework conventions, business logic patterns

#### **java.instructions.md**

- **Scope**: `**/*.java`
- **Guidelines**: Java 11 features, Moqui Java patterns, custom classes, utility implementations

#### **vuejs3.instructions.md**

- **Scope**: `**/*.vue`, `**/*.ts`, `**/*.js`, `**/*.scss`
- **Guidelines**: Vue.js 3 Composition API, TypeScript, Quasar components, state management, accessibility

#### **typescript-5-es2022.instructions.md**

- **Scope**: `**/*.ts`
- **Guidelines**: TypeScript 5.x best practices, ES2022 features, type safety, interface design

### Cross-Cutting Instructions

#### **security-and-owasp.instructions.md**

- **Scope**: All files
- **Guidelines**: OWASP Top 10, input validation, authentication/authorization, secrets management, secure coding

#### **performance-optimization.instructions.md**

- **Scope**: All files
- **Guidelines**: Entity caching, view-entity optimization, service performance, frontend optimization, database tuning

#### **code-review-generic.instructions.md**

- **Scope**: All files
- **Guidelines**: Code quality, clean code principles, testing standards, documentation, architectural alignment

### Specialized Instructions

#### **java-mcp-server.instructions.md**

- **Guidelines**: Model Context Protocol servers in Java, reactive streams, Moqui integration

#### **markdown.instructions.md**

- **Guidelines**: Documentation standards, ADR formats, technical writing, content organization

#### **localization.instructions.md**

- **Guidelines**: Internationalization, Moqui l10n patterns, translation management, multi-language support

## Agent Collaboration Patterns

### Primary Development Workflow

1. **Component Architecture** (`moqui-architect`)
   - Define entity models and relationships
   - Design service interfaces and screen flows
   - Create component boundaries and responsibilities

2. **Backend Implementation** (`moqui-developer` + `moqui-pair-navigator`)
   - Implement Moqui services with Groovy, using pairing for quality
   - Create entity definitions and view-entities
   - Develop REST APIs with validation and error handling

3. **Frontend Implementation** (`vue-frontend-specialist`)
   - Create Vue.js 3 components with Composition API
   - Implement TypeScript interfaces and state management
   - Build Quasar-based UI components

4. **Security Integration** (`security-specialist`)
   - Configure Moqui authentication and authorization
   - Define screen and service access rules (artifact authz)
   - Ensure secure REST endpoints and input validation

5. **Database Optimization** (`moqui-database-specialist`)
   - Design efficient entity models
   - Create optimized view-entities
   - Configure database caching and indexing

6. **Quality & Testing** (`moqui-testing-specialist`, `frontend-testing-specialist`)
   - Create JUnit tests for Moqui services
   - Create Jest tests for Vue.js components
   - Validate integration between frontend and backend

7. **Deployment & Containerization** (`containerization-specialist`)
   - Build Docker images for Moqui application
   - Configure PostgreSQL/MySQL containers
   - Manage Docker Compose development environment

## Best Practices Summary

### Component Design

- Follow Moqui component structure (entity/, service/, screen/, template/, data/)
- Use entity definitions for data modeling with proper relationships
- Implement view-entities for complex queries and reporting
- Design screens with proper transitions and parameter passing

### Security

- Configure artifact authorization for screens and services
- Use Moqui's built-in authentication (UserAccount, UserGroup)
- Implement field-level security with entity view restrictions
- Follow OWASP guidelines for input validation and XSS prevention

### Performance

- Leverage Moqui's entity caching (one, list, count caches)
- Use view-entities instead of multiple service calls
- Implement proper database indexing on entity fields
- Optimize Groovy service logic and avoid N+1 queries

### Quality

- Follow Moqui Framework conventions (CamelCase services, entity naming)
- Implement comprehensive JUnit tests for services
- Use Vue.js Composition API patterns consistently
- Ensure proper error handling in services and screens

### Deployment

- Create optimized Docker images for Moqui runtime
- Configure proper database connection pooling
- Use environment variables for configuration (conf/MoquiProductionConf.xml)
- Implement health checks for runtime/component status

## Quick Reference Commands

### Development

```bash
# Build Moqui Framework
./gradlew build

# Load initial data
./gradlew load

# Run Moqui runtime
./gradlew run

# Clean and rebuild
./gradlew clean build

# Run specific component tests
./gradlew test --tests "moqui.basic.EntityCrudTests"
```

### Docker Development

```bash
# Start Moqui with PostgreSQL
docker-compose -f docker/moqui-postgres-compose.yml up -d

# View Moqui logs
docker-compose logs -f moqui

# Restart Moqui container
docker-compose restart moqui

# Clean restart environment
docker-compose down -v && docker-compose up -d
```

### Database Operations

```bash
# Run entity sync (create/update tables)
./gradlew runtime:component:tools:entity-sync

# Load component data
./gradlew load -PdataTypes=demo

# Export entity data
./gradlew runtime:component:tools:entity-export
```

This steering summary provides comprehensive guidance for all aspects of Durion Frontend Platform development, from architecture and implementation to testing and deployment. Reference this document for consistent development practices across all Moqui components and Vue.js frontend work.
