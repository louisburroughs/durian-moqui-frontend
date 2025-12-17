# Glossary

## Core Moqui Terminology

- Moqui Framework: Enterprise application framework for building database-backed business applications with XML entity/service definitions and Groovy scripting.
- Entity: Data model definition in Moqui (XML); represents database tables and relationships.
- Service: Business logic definition in Moqui (XML); can be implemented in Groovy, Java, or script.
- Screen: UI definition in Moqui (XML); renders web pages using FreeMarker templates and Vue.js components.
- Component: Modular Moqui application package containing entities, services, screens, and resources.
- Runtime: Directory containing Moqui components, configuration, database, and runtime data.
- mantle-udm: Universal Data Model; core entity definitions for parties, products, orders, accounting.
- mantle-usl: Universal Service Library; reusable service patterns and business logic.
- FreeMarker: Template engine used by Moqui for server-side rendering (.ftl files).
- Groovy: JVM scripting language used for Moqui service implementations and business logic.
- Entity Facade: Moqui API for database operations; provides abstraction over JDBC.
- Service Facade: Moqui API for invoking services; handles transactions, authorization, and execution.
- Screen Facade: Moqui API for rendering screens and managing UI state.

## Durion Application Terminology

- Durion: Operational subsidiary of TIOTF providing technology solutions for tire industry.
- TIOTF: Tire Industry Open Technology Foundation; 501(c)(6) non-profit for open tire industry technology.
- durion-accounting: Durion component for financial accounting, GL, AR/AP.
- durion-common: Shared UI components, styles, utilities, and libraries for Durion.
- durion-crm: Customer relationship management component for Durion.
- durion-demo-data: Sample/demo datasets for Durion components.
- durion-experience: Frontend experience layer (themes, layouts, navigation).
- durion-inventory: Inventory management component (stock, movements, locations).
- durion-positivity: Moqui component providing integration layer for connecting Durion frontend to durion-positivity-backend services.
- durion-positivity-backend: Separate Spring Boot microservices backend repository providing business logic and data persistence (Java 21).
- durion-product: Product master data and catalog management.
- durion-theme: Theme assets (CSS, images, fonts) and branding configuration.
- durion-workexec: Work execution and shop operations management.

## Standard Moqui Components

- PopCommerce: E-commerce component (orders, products, pricing, customers).
- HiveMind: Project management and collaboration component (tasks, wikis, time tracking).
- MarbleERP: Manufacturing and MRP component (production, BOM, work centers).
- SimpleScreens: Reusable dashboard and UI component library.
- moqui-fop: Apache FOP integration for PDF/document rendering.

## Frontend Technology

- Vue.js 3: Progressive JavaScript framework for building user interfaces.
- Composition API: Vue.js 3 API style for organizing component logic with setup(), ref(), reactive(), and composables.
- Quasar v2: Vue.js 3 component framework with Material Design for responsive web/mobile applications.
- TypeScript: Typed superset of JavaScript for type-safe frontend development.
- Single File Component (SFC): Vue.js component format (.vue files) containing template, script, and style.

## Testing Frameworks

- Spock Framework: Groovy-based BDD testing framework for Moqui backend services and business logic.
- Jest: JavaScript testing framework for Vue.js frontend components and TypeScript code.
- BDD (Behavior-Driven Development): Testing approach using given-when-then specifications.

## Domain-Driven Design

- Domain: Logical grouping of related business capabilities (e.g., accounting, inventory, CRM).
- Bounded Context: Explicit boundary within which a domain model is defined and applicable.
- Entity (DDD): Domain object with unique identity and lifecycle.
- Service (DDD): Stateless operation implementing business logic.
- Repository: Data access abstraction for entity persistence.
- Aggregate: Cluster of entities and value objects treated as a single unit for data changes.

## Integration & API

- REST API: Representational State Transfer; architectural style for web service APIs.
- Positivity Backend: Java 21 Spring Boot microservices providing backend business logic.
- durion-positivity Integration: Component bridging Durion frontend with Positivity services via REST.
- API Contract: Formal specification of request/response formats and behavior for REST endpoints.
- DTO (Data Transfer Object): Plain object for transferring data between frontend and backend.

## Development & Build

- Gradle: Build automation tool for JVM projects; used by Moqui for backend builds.
- gradlew: Gradle wrapper script for consistent Gradle version across environments.
- npm: Node.js package manager for frontend dependency management.
- Docker Compose: Tool for defining and running multi-container Docker applications.
- PostgreSQL: Open-source relational database; primary database for Moqui/Durion.
- MySQL: Alternative relational database supported by Moqui Framework.
- Hot Reload: Development feature that automatically reloads code changes without full restart.
- Build Task: Gradle or npm script that compiles, tests, and packages application code.

## Context Management Terminology

- Context Integrity: Validation that all required project information is available before providing guidance or making decisions.
- Session Context: Temporary working document (`.ai/session.md`) maintaining continuity across multi-step development tasks.
- Context Re-anchoring: Process of returning to authoritative project files when context becomes insufficient or contradictory.
- Stop-Phrase: Mandatory interruption mechanism used by pair programming agents to halt problematic implementation patterns.
- Loop Detection: Automated identification of repetitive or stalled implementation progress requiring intervention.
- Architectural Drift: Deviation from established design patterns and domain boundaries during implementation.

## Property-Based Testing Terminology

- Correctness Property: Formal statement about system behavior that should hold true across all valid executions.
- Property-Based Test (PBT): Automated test that validates correctness properties across randomly generated inputs using frameworks like jqwik.
- jqwik: Property-based testing framework for Java used to validate universal properties with configurable iteration counts.
- Domain Coverage Property: Correctness property ensuring all required agent domains are available for guidance requests.
- Guidance Quality Property: Correctness property validating that agent recommendations follow established patterns and best practices.
- Collaboration Consistency Property: Correctness property ensuring multi-agent recommendations are consistent and conflict-free.
