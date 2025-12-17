package agents.core

import groovy.transform.CompileStatic
import java.util.concurrent.ConcurrentHashMap
import java.time.Instant

/**
 * Performance Monitoring Agent
 * 
 * Collects metrics and provides alerting for agent performance.
 * Requirements: REQ-009 (Performance monitoring)
 * Test Cases: TC-026
 */
@CompileStatic
class PerformanceMonitoring {
    
    private final ConcurrentHashMap<String, MetricData> metrics = new ConcurrentHashMap<>()
    private final List<AlertRule> alertRules = []
    private final List<Alert> activeAlerts = []
    
    // Metric collection
    void recordResponseTime(String agentId, long responseTimeMs) {
        getOrCreateMetric(agentId).recordResponseTime(responseTimeMs)
        checkAlerts(agentId)
    }
    
    void recordThroughput(String agentCategory, int requestCount) {
        getOrCreateMetric(agentCategory).recordThroughput(requestCount)
    }
    
    void recordError(String agentType, String errorType) {
        getOrCreateMetric(agentType).recordError(errorType)
    }
    
    void recordResourceUsage(String resource, double utilizationPercent) {
        getOrCreateMetric(resource).recordResourceUsage(utilizationPercent)
    }
    
    // Metric retrieval
    Map<String, Double> getResponseTimePercentiles(String agentId) {
        MetricData data = metrics.get(agentId)
        return data ? data.getResponseTimePercentiles() : [p50: 0.0, p95: 0.0, p99: 0.0]
    }
    
    int getThroughput(String agentCategory) {
        MetricData data = metrics.get(agentCategory)
        return data ? data.getThroughput() : 0
    }
    
    double getErrorRate(String agentType) {
        MetricData data = metrics.get(agentType)
        return data ? data.getErrorRate() : 0.0
    }
    
    double getResourceUtilization(String resource) {
        MetricData data = metrics.get(resource)
        return data ? data.getResourceUtilization() : 0.0
    }
    
    // Alert management
    void addAlertRule(String metricKey, String condition, double threshold, String message) {
        alertRules.add(new AlertRule(metricKey, condition, threshold, message))
    }
    
    List<Alert> getActiveAlerts() {
        return new ArrayList<>(activeAlerts)
    }
    
    void clearAlert(String alertId) {
        activeAlerts.removeIf { it.id == alertId }
    }
    
    // Private helpers
    private MetricData getOrCreateMetric(String key) {
        return metrics.computeIfAbsent(key) { new MetricData(key) }
    }
    
    private void checkAlerts(String metricKey) {
        MetricData data = metrics.get(metricKey)
        if (!data) return
        
        alertRules.each { rule ->
            if (rule.metricKey == metricKey && rule.isTriggered(data)) {
                activeAlerts.add(new Alert(
                    UUID.randomUUID().toString(),
                    rule.message,
                    metricKey,
                    Instant.now()
                ))
            }
        }
    }
    
    // Inner classes
    static class MetricData {
        final String key
        final List<Long> responseTimes = []
        int throughputCount = 0
        int errorCount = 0
        int totalRequests = 0
        double resourceUtilization = 0.0
        
        MetricData(String key) {
            this.key = key
        }
        
        synchronized void recordResponseTime(long ms) {
            responseTimes.add(ms)
            totalRequests++
            if (responseTimes.size() > 1000) responseTimes.remove(0)
        }
        
        synchronized void recordThroughput(int count) {
            throughputCount += count
        }
        
        synchronized void recordError(String errorType) {
            errorCount++
            totalRequests++
        }
        
        synchronized void recordResourceUsage(double percent) {
            resourceUtilization = percent
        }
        
        Map<String, Double> getResponseTimePercentiles() {
            if (responseTimes.isEmpty()) return [p50: 0.0, p95: 0.0, p99: 0.0]
            
            List<Long> sorted = new ArrayList<>(responseTimes).sort()
            int size = sorted.size()
            
            return [
                p50: sorted[(int)(size * 0.50)].doubleValue(),
                p95: sorted[(int)(size * 0.95)].doubleValue(),
                p99: sorted[(int)(size * 0.99)].doubleValue()
            ]
        }
        
        int getThroughput() {
            return throughputCount
        }
        
        double getErrorRate() {
            return totalRequests > 0 ? (errorCount / (double)totalRequests) * 100.0 : 0.0
        }
        
        double getResourceUtilization() {
            return resourceUtilization
        }
    }
    
    static class AlertRule {
        final String metricKey
        final String condition
        final double threshold
        final String message
        
        AlertRule(String metricKey, String condition, double threshold, String message) {
            this.metricKey = metricKey
            this.condition = condition
            this.threshold = threshold
            this.message = message
        }
        
        boolean isTriggered(MetricData data) {
            double value = getMetricValue(data)
            switch (condition) {
                case 'gt': return value > threshold
                case 'lt': return value < threshold
                case 'gte': return value >= threshold
                case 'lte': return value <= threshold
                default: return false
            }
        }
        
        private double getMetricValue(MetricData data) {
            if (metricKey.contains('response-time')) {
                return data.getResponseTimePercentiles().p95
            } else if (metricKey.contains('error-rate')) {
                return data.getErrorRate()
            } else if (metricKey.contains('resource')) {
                return data.getResourceUtilization()
            }
            return 0.0
        }
    }
    
    static class Alert {
        final String id
        final String message
        final String metricKey
        final Instant timestamp
        
        Alert(String id, String message, String metricKey, Instant timestamp) {
            this.id = id
            this.message = message
            this.metricKey = metricKey
            this.timestamp = timestamp
        }
    }
}
