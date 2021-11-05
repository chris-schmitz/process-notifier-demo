package com.schmitz.processnotifierdemo.dto;

public class ProcessingUpdateResponse {
    String entityName;
    String from;
    ProcessingStages type;

    public ProcessingUpdateResponse(String entityName, String from, ProcessingStages type) {
        this.entityName = entityName;
        this.from = from;
        this.type = type;
    }

    public ProcessingUpdateResponse generate(String entityName, String from, ProcessingStages type) {
        return new ProcessingUpdateResponse(entityName, from, type);
    }

    // TODO: figure out if these are actually needed for the simpMessageTemplate
    public String getEntityName() {
        return entityName;
    }

    public String getFrom() {
        return from;
    }

    public ProcessingStages getType() {
        return type;
    }
}
