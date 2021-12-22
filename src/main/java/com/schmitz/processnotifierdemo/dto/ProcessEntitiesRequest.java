package com.schmitz.processnotifierdemo.dto;

import java.util.List;
import java.util.Map;

public class ProcessEntitiesRequest {
    private List<Map<String, String>> names;

    public ProcessEntitiesRequest() {
    }

    public ProcessEntitiesRequest(List<Map<String, String>> names) {
        this.names = names;
    }

    public List<Map<String, String>> getNames() {
        return names;
    }
}
