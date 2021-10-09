package com.schmitz.processnotifierdemo.dto;

public class Response {
    String content;
    String from;

    public Response(String content, String from) {
        this.content = content;
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }
}
