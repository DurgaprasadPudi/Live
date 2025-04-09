package com.hetero.heteroiconnect;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication 
@EnableCaching
@EnableRetry
@EnableAsync
@EnableJpaAuditing          // Enable JPA Auditing for automatic date auditing (created/modified timestamps)
@EnableScheduling           // Enable Spring Scheduling for periodic tasks
@CrossOrigin(origins = { "http://172.19.1.84:4200" })  // Allow cross-origin requests from the specified origin
public class HeteroiconnectApplication extends SpringBootServletInitializer {

	/// Live
    private static final Logger logger = LoggerFactory.getLogger(HeteroiconnectApplication.class);

    @Autowired
    private DynamicCleanupService cleanupService;  // Dynamic cleanup service

    // Method for dynamic cleanup service registration
    public static void main(final String[] args) {
        SpringApplication.run(HeteroiconnectApplication.class, args);
    }

    // Method to configure the application for servlet-based deployment
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HeteroiconnectApplication.class);
    }

    // Registering resources during startup (example using CommandLineRunner)
    @Bean
    public CommandLineRunner run() {
        return args -> {
            // Example: Registering resources dynamically during startup
            SomeResource resource1 = new SomeResource();
            cleanupService.registerResource("Resource1", resource1);

            SomeResource resource2 = new SomeResource();
            cleanupService.registerResource("Resource2", resource2);

            logger.info("Resources have been registered and will be cleaned up periodically.");
        };
    }

    // Periodic cleanup logic: this method runs every 1 hour (3600000 milliseconds)
    @Scheduled(fixedRate = 3600000)
    public void periodicCleanup() {
        logger.info("Performing periodic cleanup...");
        cleanupService.periodicCleanup();  // Perform periodic cleanup of registered resources
    }

    // Final cleanup before shutdown: cleans up resources when the application shuts down
    @PreDestroy
    public void cleanup() {
        logger.info("Application is shutting down. Performing final cleanup...");
        cleanupService.cleanup();  // Perform final cleanup before shutdown
    }
}

// Service to handle dynamic cleanup logic
@Service
class DynamicCleanupService {

    private Map<String, CleanupResource> resourceRegistry = new HashMap<>();

    // Register resources that need cleanup
    public void registerResource(String resourceName, CleanupResource resource) {
        resourceRegistry.put(resourceName, resource);
        System.out.println("Resource registered: " + resourceName);
    }

    // Periodic cleanup for registered resources
    public void periodicCleanup() {
        for (Map.Entry<String, CleanupResource> entry : resourceRegistry.entrySet()) {
            CleanupResource resource = entry.getValue();
            if (resource != null) {
                resource.cleanup();  // Perform cleanup
                System.out.println("Resource cleaned: " + entry.getKey());
            }
        }
    }

    // Final cleanup on shutdown
    @PreDestroy
    public void cleanup() {
        for (Map.Entry<String, CleanupResource> entry : resourceRegistry.entrySet()) {
            CleanupResource resource = entry.getValue();
            if (resource != null) {
                resource.close();  // Perform final cleanup
                System.out.println("Final cleanup for resource: " + entry.getKey());
            }
        }
    }
}
