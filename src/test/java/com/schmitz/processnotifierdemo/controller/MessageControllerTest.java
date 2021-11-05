package com.schmitz.processnotifierdemo.controller;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessEntityRequest;
import com.schmitz.processnotifierdemo.factories.EntityFactory;
import com.schmitz.processnotifierdemo.service.EntityProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    EntityFactory entityFactory;

    @Mock
    EntityProcessorService processorService;

    @InjectMocks
    MessageController controller;

    // TODO: replace with some kind of authenticated user setup
    private String username = "some user";

    @Test
    void processEntity_entitySucceeds() throws InterruptedException {
        ProcessEntityRequest request = new ProcessEntityRequest("entity A");
        Entity entity = new Entity(username, request.getName());
        when(entityFactory.build(username, request.getName())).thenReturn(entity);

        controller.processEntity(request);

        verify(processorService).processEntity(entity);
    }

    @Test
    void processEntity_entityFails() {

    }

    @Test
    void processEntities_entitiesSucceed() {

    }

    @Test
    void processEntities_entitiesFail() {

    }
}