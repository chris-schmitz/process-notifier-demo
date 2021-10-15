package com.schmitz.processnotifierdemo.dto;

public class ProcessEntityResponse {
    String entityName;
    String from;
    ResponseType type;


    public ProcessEntityResponse(String entityName, String from, ResponseType type) {
        this.entityName = entityName;
        this.from = from;
        this.type = type;
    }

    public ProcessEntityResponse generate(String entityName, String from, ResponseType type) {
        return new ProcessEntityResponse(entityName, from, type);
    }

    // TODO: figure out if these are actually needed for the simpMessageTemplate
    public String getEntityName() {
        return entityName;
    }

    public String getFrom() {
        return from;
    }

    public ResponseType getType() {
        return type;
    }
}
