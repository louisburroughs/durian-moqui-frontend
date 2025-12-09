#!/usr/bin/env node

/**
 * MCP Server: Moqui Agents Information
 * 
 * Provides access to:
 * - Agent descriptions and capabilities
 * - Agent collaboration patterns
 * - Agent workflow guidelines
 * - Cross-agent communication
 */

const { Server } = require('@modelcontextprotocol/sdk/server/stdio.js');
const { StdioServerTransport } = require('@modelcontextprotocol/sdk/server/stdio.js');
const {
  CallToolRequestSchema,
  TextContent,
  ErrorCode,
  McpError,
} = require('@modelcontextprotocol/sdk/types.js');
const fs = require('fs');
const path = require('path');

const projectRoot = process.env.MOQUI_PROJECT_ROOT || '/home/n541342/IdeaProjects/moqui_example';
const agentsPath = path.join(projectRoot, process.env.AGENTS_PATH || '.github/agents');

const server = new Server({
  name: 'moqui-agents',
  version: '1.0.0',
});

const agents = {
  architecture_agent: 'Chief Architect - Domain-driven design and architectural integrity',
  moqui_developer_agent: 'Moqui Implementation Expert - Turns architecture and design into working code',
  dba_agent: 'Expert Database Administrator - Performance tuning, schema design, and database security',
  sre_agent: 'SRE/Observability Agent - Functional & Operational Metrics, OpenTelemetry, Grafana Integration',
  test_agent: 'QA Software Engineer - Writes, runs, and analyzes tests',
  lint_agent: 'Code Quality Engineer - Style enforcement and static analysis',
  api_agent: 'Senior Software Engineer - REST API development and error handling',
  dev_deploy_agent: 'Senior DevOps Engineer - Local development deployment and containerization',
  docs_agent: 'Expert Technical Writer - Documentation and knowledge base'
};

server.setRequestHandler(CallToolRequestSchema, async (request) => {
  if (request.params.name === 'list_agents') {
    const agentsList = Object.entries(agents).map(([name, desc]) => ({
      name,
      description: desc
    }));
    return {
      content: [
        {
          type: 'text',
          text: JSON.stringify(agentsList, null, 2)
        }
      ]
    };
  }

  if (request.params.name === 'get_agent_description') {
    const agentName = request.params.arguments?.agent;
    if (!agentName || !agents[agentName]) {
      throw new McpError(ErrorCode.InvalidParams, `Unknown agent: ${agentName}`);
    }

    try {
      const agentFile = path.join(agentsPath, `${agentName.replace(/_/g, '-')}.md`);
      const content = fs.readFileSync(agentFile, 'utf-8');
      return {
        content: [
          {
            type: 'text',
            text: content
          }
        ]
      };
    } catch (e) {
      throw new McpError(ErrorCode.InternalError, `Error reading agent file: ${e.message}`);
    }
  }

  if (request.params.name === 'get_collaboration_framework') {
    try {
      const collabFile = path.join(projectRoot, '.github/AGENT_COLLABORATION.md');
      const content = fs.readFileSync(collabFile, 'utf-8');
      return {
        content: [
          {
            type: 'text',
            text: content
          }
        ]
      };
    } catch (e) {
      throw new McpError(ErrorCode.InternalError, `Error reading collaboration framework: ${e.message}`);
    }
  }

  throw new McpError(
    ErrorCode.MethodNotFound,
    `Unknown tool: ${request.params.name}`
  );
});

async function main() {
  const transport = new StdioServerTransport();
  await server.connect(transport);
  console.error('Moqui Agents MCP server running on stdio');
}

main().catch(console.error);
