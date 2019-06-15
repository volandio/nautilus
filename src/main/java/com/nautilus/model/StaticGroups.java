package com.nautilus.model;

public enum StaticGroups {

    ADMIN_GROUP("ADMIN"), USER_GROUP("USER");

    private String name;

    StaticGroups(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
