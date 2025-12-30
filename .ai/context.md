# Project Context

## Overview

Durion Frontend Platform (durion-moqui-frontend) is a modern web application built on Moqui Framework for the Tire Industry Open Technology Foundation (TIOTF). This project provides responsive web interfaces for ERP, e-commerce, project management, and manufacturing operations using Vue.js 3, Quasar, and TypeScript with a Java 11 backend.

## Goals

- Provide modern, responsive web interfaces for tire industry ERP operations
- Deliver modular, component-based UI/UX for e-commerce, manufacturing, and project management
- Integrate seamlessly with Positivity backend services via REST APIs
- Support domain-driven design with clear boundaries between business domains
- Enable vendor-neutral, open technology for tire, fleet, and service-provider industries

## Tech Stack

- **Backend:** Java 11, Moqui Framework 3.x, Groovy scripting
- **Frontend:** Vue.js 3.x, Quasar v2.x, TypeScript 5.x
- **Templating:** FreeMarker (.ftl)
- **Build:** Gradle (java with `./gradlew`), npm/yarn (frontend)
- **Database:** PostgreSQL (primary), MySQL (supported)
- **Container:** Docker Compose for local development
- **Testing:** Spock Framework (backend BDD), Jest (frontend)

## Repository Structure (high-level)

### Moqui Framework Core

- `framework/` — Moqui Framework core (entities, services, screens, Java/Groovy source)
- `runtime/conf/` — Configuration files
- `runtime/db/` — Database scripts
- `runtime/lib/` — Runtime libraries

### Core Moqui Components

- `runtime/component/PopCommerce/` — E-commerce domain (orders, products, pricing)
- `runtime/component/HiveMind/` — Project management and collaboration
- `runtime/component/mantle-udm/` — Universal Data Model (core entities)
- `runtime/component/mantle-usl/` — Universal Service Library (reusable patterns)
- `runtime/component/moqui-fop/` — FOP integration for document rendering

### Durion-Specific Components

- `runtime/component/durion-accounting/` — Durion accounting domain screens and services
- `runtime/component/durion-common/` — Shared UI, styles, and utilities for Durion
- `runtime/component/durion-crm/` — Customer relationship management
- `runtime/component/durion-demo-data/` — Demo datasets for Durion components
- `runtime/component/durion-experience/` — Frontend experience layer (themes/layouts)
- `runtime/component/durion-inventory/` — Inventory management
- `runtime/component/durion-positivity/` — Integration layer component for connecting with durion-positivity-backend services (Spring Boot microservices)
- `runtime/component/durion-product/` — Product master data and catalogs
- `runtime/component/durion-theme/` — Durion theme assets and configuration
- `runtime/component/durion-workexec/` — Work execution and operations

### Project Configuration

- `docker/` — Docker Compose files for PostgreSQL, MySQL, and deployment
- `.github/` — Documentation, ADRs, agents, and instructions
- `.vscode/` — VSCode workspace configuration

## Operational Notes

- Java 11 is required for Moqui backend; ensure local JDK 11 is active (use `.sdkmanrc` with sdkman)
- Node.js 16+ required for Vue.js/TypeScript frontend development
- Use Gradle wrapper (`./gradlew`) for backend builds
- Use npm for frontend dependency management and builds
- Docker Compose provides local PostgreSQL/MySQL for development
- Default runtime directory is created by Gradle tasks; not in source repository
- Access application at `http://localhost:8080/webroot/` after starting

## Adjacent Projects

- `durion-positivity-backend` — Java 21 Spring Boot microservices backend (separate repository); provides business logic and data persistence; integrates with Moqui via REST APIs
- `durion/workspace-agents` — Java 11 Gradle project for agents/testing

## Security & Compliance

- Secrets must come from environment variables or secret stores; never hardcoded
- Follow OWASP best practices for input validation, authorization, and logging
- Use parameterized queries in Groovy services; never use SQL string concatenation
- Session cookies configured with HttpOnly, Secure, and SameSite attributes
- Moqui provides built-in authentication/authorization; leverage framework security patterns

## Development Guidance

- **Frontend:** Follow Vue.js 3 Composition API patterns with TypeScript (see `.github/instructions/vuejs3.instructions.md`)
- **Backend:** Follow Moqui Framework conventions for entity, service, and screen definitions
- **Services:** Use Moqui service definitions (XML); implement complex logic in Groovy
- **Entities:** Define entities in XML following Moqui patterns; leverage mantle-udm where possible
- **Screens:** Use Moqui XML screen definitions with FreeMarker templates
- **REST APIs:** Expose via Moqui REST services; follow consistent patterns for integration with durion-positivity-backend
- **Testing:** Write JUnit tests for Groovy services; Jest tests for Vue components
- **Domain Boundaries:** Maintain clear separation between Durion domains (accounting, CRM, inventory, etc.)
- **Integration:** Use durion-positivity component for all durion-positivity-backend service integration

## Context Management Rules

### Primary Rules

- If required inputs are missing from current context:
  SAY: "Context insufficient – re-anchor needed"
- If referring to decisions not found in:
  - current files
  - /ai/context.md
  - /ai/glossary.md
  STOP

### Temporary Context Store Rules

**Purpose:** Minimize redundant file reads and maintain continuity across multi-step tasks.

1. **Context Store Location:** Maintain `.ai/session.md` as a temporary working document for the current development session
2. **Session Initialization:** At the start of each task:
   - Check if `.ai/session.md` exists and is recent (updated within current session)
   - If yes: READ `.ai/session.md` first before re-reading project files
   - If no or stale: BEGIN from `.ai/context.md` and `.ai/glossary.md`
3. **What to Store in Session:**
   - Current task objective and status
   - Key architectural decisions made in this session
   - Recent file paths and structures accessed
   - Active requirements/constraints being addressed
   - Integration points or dependencies discovered
   - Open questions or blockers
4. **Session Updates:** After completing any subtask or making significant progress:
   - UPDATE `.ai/session.md` with findings, decisions, and next steps
   - INCLUDE: timestamp, current file state, discovered patterns, decisions made
   - OMIT: full file contents (link instead)
5. **Session Cleanup:** At task completion or session end:
   - PRESERVE key decisions and learnings in `.ai/context.md` if they're durable
   - DELETE or ARCHIVE `.ai/session.md` when starting a new unrelated task
6. **Conflict Resolution:** If session context contradicts project context:
   - Trust the permanent files (`.ai/context.md`, `.ai/glossary.md`, source files)
   - UPDATE session.md to reflect the authoritative state
7. **Large Tasks:** For multi-file edits or complex deployments:
   - Create an `.ai/session-{task-id}.md` variant if the session spans hours or multiple contexts
   - Link it in the main `.ai/session.md` for continuity
