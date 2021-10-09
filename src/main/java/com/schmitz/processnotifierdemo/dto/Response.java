package com.schmitz.processnotifierdemo.dto;

public class Response {
    String content;
    String from;
    ResponseType type;

    public Response(String content, String from, ResponseType type) {
        this.content = content;
        this.from = from;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }

    public ResponseType getType() {
        return type;
    }
}
