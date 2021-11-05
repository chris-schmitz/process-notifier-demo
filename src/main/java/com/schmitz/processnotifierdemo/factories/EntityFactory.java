package com.schmitz.processnotifierdemo.factories;

import com.schmitz.processnotifierdemo.dto.Entity;
import org.springframework.stereotype.Component;

@Component
public class EntityFactory {

    public Entity build(String sender, String entityName) {
        return new Entity(sender, entityName);
    }
}
