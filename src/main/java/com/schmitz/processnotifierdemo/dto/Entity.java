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

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
