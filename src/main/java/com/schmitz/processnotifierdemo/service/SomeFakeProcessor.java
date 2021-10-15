package com.schmitz.processnotifierdemo.service;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessEntityResponse;
import com.schmitz.processnotifierdemo.dto.ResponseType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.Random;

public class SomeFakeProcessor {

    private static SomeFakeProcessor instance;
    private Integer sleepMax = 5;
    private Integer sleepMin = 1;

    private SimpMessagingTemplate simpMessagingTemplate;
    private ProcessEntityResponse response;

    public SomeFakeProcessor(SimpMessagingTemplate simpMessagingTemplate, ProcessEntityResponse response) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.response = response;
    }


    public void process(Entity entity) throws InterruptedException {
        Arrays.stream(ResponseType.values()).forEach(type -> {
            try {
                this.sleep();
                simpMessagingTemplate.convertAndSend(response.generate(entity.getName(), entity.getFrom(), type));
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
