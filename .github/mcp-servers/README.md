# MCP Server Configuration for Moqui Project

This directory contains Model Context Protocol (MCP) server implementations for the Moqui project.

## Overview

MCP servers provide Copilot and other AI assistants with standardized access to project-specific information and tools. Each server handles a specific domain of knowledge.

## Available MCP Servers

### 1. moqui-context-server.js
**Purpose:** Project-wide context information

**Tools:**
- `get_project_info` - Returns project metadata, components, agents, tech stack
- `get_durion_components` - Lists all Durion business components
- `get_technology_stack` - Detailed tech stack information (frontend, backend, database, devops)

**Use Cases:**
- Providing agents with project structure context
- Understanding component organization
- Technology compatibility checks

### 2. moqui-agents-server.js
**Purpose:** Agent descriptions, relationships, and collaboration patterns

**Tools:**
- `list_agents` - Lists all available agents with descriptions
- `get_agent_description` - Returns full agent documentation
- `get_agent_relationships` - Shows how agents collaborate
- `get_collaboration_framework` - Returns AGENT_COLLABORATION.md

**Use Cases:**
- Understanding agent roles and responsibilities
- Agent-to-agent communication patterns
- Workflow and escalation procedures
- Finding the right agent for a task

### 3. project-analysis-server.js
**Purpose:** Component analysis, dependencies, and architecture

**Tools:**
- `list_durion_components` - Lists all Durion components with metadata
- `get_component_info` - Returns component details (tier, description, entities, dependencies)
- `get_component_dependencies` - Lists what a component depends on
- `get_architecture_docs` - Lists available architecture documentation
- `get_layering_rules` - Returns architectural layering rules and domain boundaries

**Use Cases:**
- Component dependency analysis
- Architecture compliance checking
- Cross-domain relationship validation
- Service placement decisions

## Setup Instructions

### Prerequisites

```bash
npm install @modelcontextprotocol/sdk
```

### Installation with GitHub Copilot in VS Code

This is the **recommended** approach for Moqui development:

1. **Edit VS Code Settings** (`Ctrl+Shift+P` → "Preferences: Open User Settings (JSON)"):
   ```json
   "chat.mcp.enabled": {
     "moqui-context": {
       "command": "node",
       "args": ["/home/n541342/IdeaProjects/moqui_example/.github/mcp-servers/moqui-context-server.js"],
       "env": {
         "MOQUI_PROJECT_ROOT": "/home/n541342/IdeaProjects/moqui_example",
         "MOQUI_COMPONENTS_PATH": "runtime/component",
         "MOQUI_FRAMEWORK_PATH": "framework"
       }
     },
     "moqui-agents": {
       "command": "node",
       "args": ["/home/n541342/IdeaProjects/moqui_example/.github/mcp-servers/moqui-agents-server.js"],
       "env": {
         "MOQUI_PROJECT_ROOT": "/home/n541342/IdeaProjects/moqui_example",
         "AGENTS_PATH": ".github/agents",
         "DOCUMENTATION_PATH": ".github/docs"
       }
     },
     "project-analysis": {
       "command": "node",
       "args": ["/home/n541342/IdeaProjects/moqui_example/.github/mcp-servers/project-analysis-server.js"],
       "env": {
         "MOQUI_PROJECT_ROOT": "/home/n541342/IdeaProjects/moqui_example",
         "DURION_COMPONENTS": "durion-common,durion-crm,durion-product,durion-inventory,durion-accounting,durion-workexec,durion-experience,durion-positivity,durion-theme,durion-demo-data,durion-mcp"
       }
     }
   }
   ```

2. **Restart VS Code** to activate MCP servers

3. **Open Copilot Chat** (Ctrl+Shift+I) and use:
   ```
   @moqui-agents list-agents
   @moquiDeveloper-agent Help me understand Moqui entities
   @api-agent Design a REST endpoint
   ```

See [COPILOT_MCP_INTEGRATION.md](../COPILOT_MCP_INTEGRATION.md) for complete usage guide.

### Installation with Claude Desktop

1. **Copy MCP server files** to your MCP server directory:
   ```bash
   cp .github/mcp-servers/*.js /path/to/your/mcp-servers/
   ```

2. **Update your MCP configuration** (e.g., `claude_desktop_config.json`):
   ```json
   {
     "mcpServers": {
       "moqui-context": {
         "command": "node",
         "args": ["/path/to/mcp-servers/moqui-context-server.js"],
         "env": {
           "MOQUI_PROJECT_ROOT": "/home/n541342/IdeaProjects/moqui_example",
           "MOQUI_COMPONENTS_PATH": "runtime/component",
           "MOQUI_FRAMEWORK_PATH": "framework"
         }
       },
       "moqui-agents": {
         "command": "node",
         "args": ["/path/to/mcp-servers/moqui-agents-server.js"],
         "env": {
           "MOQUI_PROJECT_ROOT": "/home/n541342/IdeaProjects/moqui_example",
           "AGENTS_PATH": ".github/agents",
           "DOCUMENTATION_PATH": ".github/docs"
         }
       },
       "project-analysis": {
         "command": "node",
         "args": ["/path/to/mcp-servers/project-analysis-server.js"],
         "env": {
           "MOQUI_PROJECT_ROOT": "/home/n541342/IdeaProjects/moqui_example",
           "DURION_COMPONENTS": "durion-common,durion-crm,durion-product,durion-inventory,durion-accounting,durion-workexec,durion-experience,durion-positivity,durion-theme,durion-demo-data,durion-mcp"
         }
       }
     }
   }
   ```

3. **Verify MCP servers are working:**
   ```bash
   node .github/mcp-servers/moqui-context-server.js
   # Should output: "Moqui Context MCP server running on stdio"
   ```

## Usage Examples

### Querying Project Information

**Request:**
```json
{
  "method": "tools/call",
  "params": {
    "name": "get_project_info"
  }
}
```

**Response:**
```json
{
  "content": [{
    "type": "text",
    "text": "{...project metadata...}"
  }]
}
```

### Getting Agent Information

**Request:**
```json
{
  "method": "tools/call",
  "params": {
    "name": "get_agent_description",
    "arguments": {
      "agent": "moqui_developer_agent"
    }
  }
}
```

### Analyzing Component Dependencies

**Request:**
```json
{
  "method": "tools/call",
  "params": {
    "name": "get_component_dependencies",
    "arguments": {
      "component": "durion-crm"
    }
  }
}
```

## Environment Variables

All MCP servers read these environment variables:

- `MOQUI_PROJECT_ROOT` - Root directory of the Moqui project
- `MOQUI_COMPONENTS_PATH` - Path to components relative to root
- `MOQUI_FRAMEWORK_PATH` - Path to framework relative to root
- `AGENTS_PATH` - Path to agent definitions
- `DOCUMENTATION_PATH` - Path to architecture documentation
- `DURION_COMPONENTS` - Comma-separated list of Durion components

## Extending MCP Servers

To add new tools to existing servers or create new servers:

1. Create a new `.js` file in `.github/mcp-servers/`
2. Import the SDK:
   ```javascript
   const { Server } = require('@modelcontextprotocol/sdk/server/stdio.js');
   ```
3. Implement `setRequestHandler` with your tools
4. Update `.github/mcp-config.json` to register the new server
5. Update your client config to use the new server

## Troubleshooting

**Server won't start:**
- Check Node.js version (requires 14+)
- Verify environment variables are set correctly
- Check file paths are absolute

**Tools not found:**
- Ensure MCP config has correct command and args
- Verify server process is running
- Check tool names match exactly (case-sensitive)

**Missing data:**
- Verify MOQUI_PROJECT_ROOT points to correct location
- Check file paths for components and documentation
- Ensure agent and architecture files exist

### VS Code Specific Issues

**MCP servers not connecting in Copilot Chat:**
1. Verify absolute paths in `settings.json` (not relative paths)
2. Ensure Node.js is in your PATH: `which node`
3. Check VS Code terminal for errors: View → Toggle Panel
4. Restart VS Code completely: `Ctrl+Shift+P` → "Reload Window"

**@mcp-server not recognized:**
1. Enable MCP servers: `chat.mcp.autostart: "newAndOutdated"`
2. Allow MCP in settings: `chat.mcp.gallery.enabled: true`
3. Ensure server names in `settings.json` match usage (e.g., `@moqui-agents`)

## Future Enhancements

Potential MCP servers to add:

- **moqui-entities-server.js** - Entity definition analysis and validation
- **moqui-services-server.js** - Service definition lookup and relationships
- **moqui-tests-server.js** - Test file analysis and coverage
- **moqui-metrics-server.js** - Metrics and observability configuration
- **moqui-api-server.js** - REST API endpoint documentation

## References

- [Model Context Protocol Documentation](https://modelcontextprotocol.io/)
- [MCP SDK for JavaScript](https://github.com/modelcontextprotocol/typescript-sdk)
- [Moqui Framework Documentation](https://www.moqui.org/)
- [Agent Collaboration Framework](./../AGENT_COLLABORATION.md)
- [Copilot MCP Integration Guide](../COPILOT_MCP_INTEGRATION.md)
