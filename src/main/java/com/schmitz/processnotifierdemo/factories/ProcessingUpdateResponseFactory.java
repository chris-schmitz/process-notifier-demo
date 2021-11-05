package com.schmitz.processnotifierdemo.factories;

import com.schmitz.processnotifierdemo.dto.ProcessingStages;
import com.schmitz.processnotifierdemo.dto.ProcessingUpdateResponse;
import org.springframework.stereotype.Component;

@Component
public class ProcessingUpdateResponseFactory {
    public ProcessingUpdateResponse build(String entityName, String sender, ProcessingStages stage) {
        return new ProcessingUpdateResponse(entityName, sender, stage);
    }
}
