package com.example.javaproject.models;

public enum RoleName {
    USER("USER"),
    ADMIN("ADMIN");

    RoleName(String name) {
        this.name = name;
    }
    public final String name;
}
