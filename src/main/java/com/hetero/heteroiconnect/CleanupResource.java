package com.hetero.heteroiconnect;

// Interface for resources that need cleanup
public interface CleanupResource {
    void cleanup();  // Periodic cleanup
    void close();    // Final cleanup during shutdown
}
