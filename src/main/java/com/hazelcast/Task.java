package com.hazelcast;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Task implements Serializable {

    private final UUID id;
    private final String label;
    private final LocalDateTime created;
    private boolean done;

    public Task(String label) {
        this.label = label;
        this.id = UUID.randomUUID();
        this.created = LocalDateTime.now();
    }

    public boolean isDone() {
        return done;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public UUID getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}