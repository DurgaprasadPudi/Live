package com.hetero.heteroiconnect;

// Implementation of a sample resource
public class SomeResource implements CleanupResource {

    private boolean isOpen;

    public SomeResource() {
        this.isOpen = true;
        System.out.println("SomeResource initialized.");
    }

    @Override
    public void cleanup() {
        if (isOpen) {
            System.out.println("Cleaning up resource...");
            isOpen = false; // Mark as cleaned up
        }
    }

    @Override
    public void close() {
        if (isOpen) {
            System.out.println("Closing resource...");
            isOpen = false; // Mark as closed
        }
    }
}
