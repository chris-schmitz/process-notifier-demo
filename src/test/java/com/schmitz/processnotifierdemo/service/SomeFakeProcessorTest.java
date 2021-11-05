package com.schmitz.processnotifierdemo.service;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessingStages;
import com.schmitz.processnotifierdemo.dto.ProcessingUpdateResponse;
import com.schmitz.processnotifierdemo.factories.ProcessingUpdateResponseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SomeFakeProcessorTest {

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @Mock
    ProcessingUpdateResponseFactory responseFactory;

    @InjectMocks
    SomeFakeProcessor processor;


    @Test
    void executeProcess_expect_response_relayed_to_users() throws InterruptedException {
        Entity entity = new Entity("user1", "entity1");
        ProcessingUpdateResponse response1 = new ProcessingUpdateResponse(entity.getName(), entity.getFrom(), ProcessingStages.STAGE_1);
        ProcessingUpdateResponse response2 = new ProcessingUpdateResponse(entity.getName(), entity.getFrom(), ProcessingStages.STAGE_2);
        ProcessingUpdateResponse response3 = new ProcessingUpdateResponse(entity.getName(), entity.getFrom(), ProcessingStages.STAGE_3);
        when(responseFactory.build(entity.getName(), entity.getFrom(), ProcessingStages.STAGE_1)).thenReturn(response1);
        when(responseFactory.build(entity.getName(), entity.getFrom(), ProcessingStages.STAGE_2)).thenReturn(response2);
        when(responseFactory.build(entity.getName(), entity.getFrom(), ProcessingStages.STAGE_3)).thenReturn(response3);

        processor.process(entity);

        verify(messagingTemplate).convertAndSend(response1);
        verify(messagingTemplate).convertAndSend(response2);
        verify(messagingTemplate).convertAndSend(response3);
    }
}
