package com.hetero.heteroiconnect;

import javax.sql.DataSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class ConnectionLeakMonitor {

    private final HikariDataSource hikariDataSource;

    public ConnectionLeakMonitor(DataSource dataSource) {
        this.hikariDataSource = (HikariDataSource) dataSource;
    }

    @Scheduled(fixedRate = 60000)  // Runs every 1 minute
    public void checkLeakedConnections() {
        // For HikariCP 2.x, check the pool's current connections
        int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
        int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
        int totalConnections = hikariDataSource.getHikariPoolMXBean().getTotalConnections();
        
        // Detect potential leaks: active connections = total connections (no idle ones)
        if (activeConnections > 0 && activeConnections == totalConnections) {
            System.err.println("Potential connection leak detected! Active connections: " + activeConnections);
        }

        // Optionally, print other pool statistics
        System.out.println("Active Connections: " + activeConnections);
        System.out.println("Idle Connections: " + idleConnections);
        System.out.println("Total Connections: " + totalConnections);
    }
}
