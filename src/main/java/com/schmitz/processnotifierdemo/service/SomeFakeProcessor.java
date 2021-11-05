package com.schmitz.processnotifierdemo.service;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessingStages;
import com.schmitz.processnotifierdemo.factories.ProcessingUpdateResponseFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class SomeFakeProcessor {

    private Integer sleepMax = 5;
    private Integer sleepMin = 1;

    private SimpMessagingTemplate simpMessagingTemplate;
    private ProcessingUpdateResponseFactory responseFactory;

    public SomeFakeProcessor(SimpMessagingTemplate simpMessagingTemplate, ProcessingUpdateResponseFactory responseFactory) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.responseFactory = responseFactory;
    }


    public void process(Entity entity) throws InterruptedException {
        Arrays.stream(ProcessingStages.values()).forEach(type -> {
            try {
                this.sleep();
                simpMessagingTemplate.convertAndSend(responseFactory.build(entity.getName(), entity.getFrom(), type));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void sleep() throws InterruptedException {
        // ^ fancy unreadable handwavy way of saying "inclusive random number between min and max" ;p
        Thread.sleep(new Random().nextInt(this.sleepMax - this.sleepMin - 1) + this.sleepMin);
    }
}
