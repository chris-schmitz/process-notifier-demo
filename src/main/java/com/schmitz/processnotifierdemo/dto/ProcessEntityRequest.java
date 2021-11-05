package com.schmitz.processnotifierdemo.dto;

public class ProcessEntityRequest {
    private String name;

    public ProcessEntityRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
