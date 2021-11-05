package com.schmitz.processnotifierdemo.dto;

public class Entity {
    String from;
    String name;

    public Entity() {
    }

    public Entity(String from, String name) {
        this.from = from;
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public String getName() {
        return name;
    }
}
