package com.budget_tracker.event;

public class PrimaryButtonEvent {
    String id;

    public PrimaryButtonEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
