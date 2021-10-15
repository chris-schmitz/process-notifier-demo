package com.schmitz.processnotifierdemo.dto;

import java.util.List;

public class ProcessingCompleteResponse {
    private final List<Entity> entities;

    public ProcessingCompleteResponse(List<Entity> entities) {
        this.entities = entities;
    }

    public ProcessingCompleteResponse generate(List<Entity> entities) {
        return new ProcessingCompleteResponse(entities);
    }
}
