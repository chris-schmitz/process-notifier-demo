package com.schmitz.processnotifierdemo.dto;

import java.util.List;

public class ProcessingCompleteResponse {
    private final List<Entity> successful;
    private final List<Entity> failed;

    public ProcessingCompleteResponse(List<Entity> successful, List<Entity> failed) {
        this.successful = successful;
        this.failed = failed;
    }
}
