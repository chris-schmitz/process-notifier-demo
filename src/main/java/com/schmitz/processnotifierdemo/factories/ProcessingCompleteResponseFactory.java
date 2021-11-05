package com.schmitz.processnotifierdemo.factories;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessingCompleteResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessingCompleteResponseFactory {
    public ProcessingCompleteResponse build(List<Entity> successful, List<Entity> failed) {
        return new ProcessingCompleteResponse(successful, failed);
    }
}
