package com.schmitz.processnotifierdemo.service;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessingCompleteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EntityProcessorServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ProcessingCompleteResponse response;

    @Mock
    private SomeFakeProcessor fakeProcessor;

    private EntityProcessorService processorService;

    @BeforeEach
    void setUp() {
        processorService = new EntityProcessorService(messagingTemplate, fakeProcessor, response);
    }

    @Test
    void processEntity() throws InterruptedException {
        Entity entity = new Entity("some user", "some entity");
        ArrayList<Entity> entities = new ArrayList<>();
        String messageTopic = "/topic/messages";
        processorService.setMessageTopic(messageTopic);

        processorService.processEntity(entity);

        verify(fakeProcessor).process(entity);
        verify(messagingTemplate).convertAndSendToUser(entity.getFrom(), messageTopic, response.generate(entities));
    }

    @Test
    void processEntities() throws InterruptedException {
        String from = "some user";
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(new Entity(from, "some entity"));
        entities.add(new Entity(from, "some other entity"));
        String messageTopic = "/topic/messages";
        processorService.setMessageTopic(messageTopic);

        processorService.processEntities(from, entities);

        verify(fakeProcessor, times(2)).process(any());
        verify(messagingTemplate).convertAndSendToUser(from, messageTopic, entities);
    }
}