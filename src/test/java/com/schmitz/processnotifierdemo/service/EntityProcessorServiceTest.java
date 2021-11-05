package com.schmitz.processnotifierdemo.service;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessingCompleteResponse;
import com.schmitz.processnotifierdemo.factories.ProcessingCompleteResponseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityProcessorServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ProcessingCompleteResponseFactory processingCompleteResponseFactory;

    @Mock
    private SomeFakeProcessor fakeProcessor;

    @InjectMocks
    private EntityProcessorService processorService;

    //@BeforeEach
    //void setUp() {
    //    processorService = new EntityProcessorService(messagingTemplate, fakeProcessor, response, );
    //}

    private Entity entity1 = new Entity("some user", "an entity");
    private Entity entity2 = new Entity("some user", "another entity");
    private String messageTopic = "/topic/messages";

    @Test
    void processEntity() throws InterruptedException {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(entity1);
        ProcessingCompleteResponse completedResponse = new ProcessingCompleteResponse(entities, new ArrayList<Entity>());
        when(processingCompleteResponseFactory.build(entities, new ArrayList<Entity>())).thenReturn(completedResponse);
        processorService.setMessageTopic(messageTopic);

        processorService.processEntity(entity1);

        verify(fakeProcessor).process(entity1);
        verify(messagingTemplate).convertAndSendToUser(entity1.getFrom(), messageTopic, completedResponse);
    }

    @Test
    void processEntities() throws InterruptedException {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);
        String messageTopic = "/topic/messages";
        ProcessingCompleteResponse completedResponse = new ProcessingCompleteResponse(entities, new ArrayList<Entity>());
        when(processingCompleteResponseFactory.build(entities, new ArrayList<Entity>())).thenReturn(completedResponse);
        processorService.setMessageTopic(messageTopic);

        processorService.processEntities(entity1.getFrom(), entities);

        verify(fakeProcessor).process(entity1);
        verify(fakeProcessor).process(entity2);
        verify(messagingTemplate).convertAndSendToUser(entity1.getFrom(), messageTopic, processingCompleteResponseFactory.build(entities, new ArrayList<Entity>()));
    }

    //    TODO: come back and add tests that assert processing fails
}