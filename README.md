# Durion Moqui Frontend

**The Unified UI and Runtime Application for the Durion Platform.**

[![Durion Platform](https://img.shields.io/badge/Durion-Platform-blue)](../durion/README.md)
[![Moqui Framework](https://img.shields.io/badge/Moqui-Framework-orange)](https://moqui.org)
[![Vue 3](https://img.shields.io/badge/Vue.js-3-4FC08D?logo=vue.js&logoColor=white)](https://vuejs.org/)
[![Quasar](https://img.shields.io/badge/Quasar-Framework-1976D2?logo=quasar&logoColor=white)](https://quasar.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Java](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.java.net/)

The **Durion Frontend Platform** (`durion-moqui-frontend`) is the user-facing layer of the Durion retail system. It serves as both the **Application Runtime** (powered by the Moqui Ecosystem) and the **Modern Web UI** (Vue 3 + Quasar). It provides the business logic orchestration, user interfaces, and integration points for the Durion ecosystem, connecting seamlessly with the `durion-positivity-backend` microservices.

## Key Features

- **Modern Web Architecture**: Single Page Application (SPA) built with Vue 3 (Composition API), TypeScript 5, and Quasar.
- **Robust Runtime**: Leveraging the Moqui Framework for data modeling, service orchestration, and rule processing.
- **Modular Design**: Business logic is encapsulated in `durion-*` components, following Domain-Driven Design (DDD) principles.
- **Backend Integration**: Bridges seamlessly to Spring Boot microservices via the `durion-positivity` integration component.
- **Enterprise Ready**: Includes built-in support for security, multi-tenancy, and localization.

## Technology Stack

This project combines a robust Java runtime with a cutting-edge frontend stack:

- **Runtime & Backend Core**: Java 11, Moqui Framework (Release 3.x), Gradle, PostgreSQL
- **User Interface**: Vue.js 3 (Composition API), Quasar Framework, TypeScript 5, Pinia (if applicable) or standardized reactive stores, Node.js 18+, Vite (via Moqui build or standalone)

## Architecture

The Moqui frontend is both a **web application** (Vue/Quasar UI served via Moqui webroot) and an **application runtime** (Moqui Framework + domain components). The `durion-positivity` component is the primary integration bridge between the Moqui runtime and the Durion POS microservices.

### Conceptual Diagram

```ascii
+---------------------------+
|      User / Browser       |
|    Vue 3 + Quasar SPA     |
+-------------+-------------+
              |
              | HTTP(S)
              v
+---------------------------+
|     Moqui Web Layer       |
| - webroot / static assets |
| - screens (UI routing)    |
| - REST endpoints          |
+-------------+-------------+
              |
              v
+---------------------------+           +-----------------------------+
|   Moqui Service Layer     |<--------->|    Moqui Entity Layer       |
| - services / workflows    |           | - entities / queries        |
| - rules / orchestration   |           | - transactions / caches     |
+-------------+-------------+           +-------------+---------------+
              |                                       |
              | (component-provided logic)            | SQL
              v                                       v
+---------------------------+                 +------------------------+
|   Domain Components       |                 |   Moqui Database       |
| runtime/component/*       |                 | (typically PostgreSQL) |
| - durion-* bounded ctx    |                 +------------------------+
| - mantle-* shared domains |
+-------------+-------------+
              |
              | outbound integration (service calls)
              v
+---------------------------+      HTTP(S) / JSON      +---------------------------+
| durion-positivity         +-------------------------->+ Durion POS API Gateway    |
| (bridge component)        |                           | (durion-positivity-backend|
+---------------------------+                           +-------------+-------------+
                                                                  |
                                                                  v
                                                   +-------------------------------+
                                                   |  pos-* microservices (Java)  |
                                                   |  + their own DBs/events      |
                                                   +-------------------------------+
```

### Layer Breakdown

- **UI layer (Vue/Quasar)** renders the user experience and communicates with Moqui via webroot/screen navigation and REST calls.
- **Moqui web layer** handles request routing, screen rendering, REST endpoints, auth/session handling, and static asset delivery.
- **Moqui service layer** orchestrates business workflows (services) across domain components.
- **Components (`runtime/component/`)** own domain-specific entities, services, screens, and UI assets (`durion-*`), plus shared ecosystem components (`mantle-*`, etc.).
- **Bridge (`durion-positivity`)** is the integration boundary for calling the POS backend. This keeps direct microservice calls out of UI/screens and centralizes contracts, auth propagation, and error handling.

## Project Structure

The repository follows the standard Moqui directory structure, with custom business logic residing in `runtime/component/`.

```plaintext
durion-moqui-frontend/
├── framework/                 # Core Moqui Framework (engine, util, etc.)
├── runtime/                   # Application runtime directory
│   ├── component/             # All application components live here
│   │   ├── durion-common/     # Shared entities, services, and utilities
│   │   ├── durion-positivity/ # Bridge to Backend (Positivity) services
│   │   ├── durion-store/      # eCommerce / Storefront logic
│   │   ├── durion-ui-admin/   # Back-office Admin UI (Vue/Quasar)
│   │   └── ... (other domains)
│   ├── base-component/        # Standard Moqui base components (webroot, tools)
│   └── ...
├── docker/                    # Docker Compose configurations
├── docs/                      # Project-specific documentation
├── build.gradle               # Main Gradle build file
└── AGENTS.md                  # Quick reference for AI Agents
```

### Key Components

- **`runtime/component/durion-*`**: These are the heart of the application. Each folder represents a bounded context or functionality module.
- **`framework`**: Contains the unbundled Moqui framework source. It is rarely modified directly.

## Quick Start

### Prerequisites

- **Java**: JDK 11+
- **Node.js**: Node 18+ (for UI asset compilation)
- **Database**: PostgreSQL (or Docker)

### Build and Run

1. **Setup & Build**
    Install dependencies and build the runtime (skipping tests for speed).

    ```bash
    npm install
    ./gradlew build -x test
    ```

2. **Run Locally**
    Start the application using the embedded web server (Jetty/Tomcat).

    ```bash
    java -jar runtime/build/libs/moqui.war
    ```

    Access the application at [http://localhost:8080](http://localhost:8080).

3. **Docker (Recommended for Dev)**
    Spin up the full stack (Moqui + Postgres) using Docker Compose.

    ```bash
    docker-compose -f docker/moqui-postgres-compose.yml up -d
    ```

4. **UI Development (Hot Reload)**
    For frontend-focused work, run the dev server:

    ```bash
    npm run dev
    ```

## Agents and Documentation

For AI agents and automated workflows, please refer to the specialized documentation in the workspace root.

- **Developer Guidelines**: [Moqui Developer Agent](../durion/.github/agents/moqui-developer.agent.md)
- **Frontend Guidelines**: [Vue agent](../durion/.github/agents/vue-agent.md) or standard Vue 3 docs.
- **SRE & Observability**: [SRE Agent](../durion/.github/agents/sre.agent.md)
- **General Workflows**: [Durion Platform Agents](../durion/AGENTS.md)

---
*Part of the Durion Omni-Channel Retail Platform.*

## Referrals

The `runtime/component/durion-*` directories are maintained as separate repositories. Use the links below to jump to the owning repo and the primary domain context it serves.

| Component | GitHub Repository | Domain Served |
| --- | --- | --- |
| `durion-accounting` | https://github.com/louisburroughs/durion-accounting | `domains/accounting` |
| `durion-crm` | https://github.com/louisburroughs/durion-crm | `domains/crm` |
| `durion-inventory` | https://github.com/louisburroughs/durion-inventory | `domains/inventory` |
| `durion-shopmgr` | https://github.com/louisburroughs/durion-shopmgr | `domains/shopmgmt` |
| `durion-workexec` | https://github.com/louisburroughs/durion-workexec | `domains/workexec` |
| `durion-hr` | https://github.com/louisburroughs/durion-hr | `domains/people` |
| `durion-sales` | https://github.com/louisburroughs/durion-sales | `domains/order`, `domains/billing`, `domains/pricing` |
| `durion-product` | https://github.com/louisburroughs/durion-product | `domains/product` (catalog concepts), `domains/pricing` (MSRP inputs) |
| `durion-positivity` | https://github.com/louisburroughs/durion-positivity | `domains/positivity` (backend integration boundary) |
| `durion-experience` | https://github.com/louisburroughs/durion-experience | `domains/crm`, `domains/workexec`, `domains/billing` (customer portal surfaces) |
| `durion-common` | https://github.com/louisburroughs/durion-common | Cross-cutting/shared (used by multiple domains) |
| `durion-demo-data` | https://github.com/louisburroughs/durion-demo-data | Cross-cutting/shared (seed/demo data for multiple domains) |
| `durion-chat` | https://github.com/louisburroughs/durion-chat | Cross-cutting (operator/developer assistant UI) |
| `durion-theme` | https://github.com/louisburroughs/durion-theme | Cross-cutting (UI theme and design system assets) |

