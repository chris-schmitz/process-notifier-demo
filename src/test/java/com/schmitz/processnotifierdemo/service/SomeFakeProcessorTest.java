package com.schmitz.processnotifierdemo.service;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessEntityResponse;
import com.schmitz.processnotifierdemo.dto.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    ProcessEntityResponse response;

    SomeFakeProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new SomeFakeProcessor(messagingTemplate, response);
    }

    @Test
    void executeProcess_expect_response_relayed_to_users() throws InterruptedException {
        Entity entity = new Entity("user1", "entity1");
        ProcessEntityResponse response1 = new ProcessEntityResponse(entity.getName(), entity.getFrom(), ResponseType.STAGE_1);
        ProcessEntityResponse response2 = new ProcessEntityResponse(entity.getName(), entity.getFrom(), ResponseType.STAGE_2);
        ProcessEntityResponse response3 = new ProcessEntityResponse(entity.getName(), entity.getFrom(), ResponseType.STAGE_3);
        when(response.generate(entity.getName(), entity.getFrom(), ResponseType.STAGE_1)).thenReturn(response1);
        when(response.generate(entity.getName(), entity.getFrom(), ResponseType.STAGE_2)).thenReturn(response2);
        when(response.generate(entity.getName(), entity.getFrom(), ResponseType.STAGE_3)).thenReturn(response3);

        processor.process(entity);

        verify(messagingTemplate).convertAndSend(response1);
        verify(messagingTemplate).convertAndSend(response2);
        verify(messagingTemplate).convertAndSend(response3);
    }
}
