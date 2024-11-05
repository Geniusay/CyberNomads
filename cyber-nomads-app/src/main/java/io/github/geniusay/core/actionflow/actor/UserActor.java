package io.github.geniusay.core.actionflow.actor;

import io.github.geniusay.core.actionflow.frame.Actor;

public class UserActor implements Actor {

    private final String id;
    private final String name;

    public UserActor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}