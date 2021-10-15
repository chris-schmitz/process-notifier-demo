package com.schmitz.processnotifierdemo.controller;

import com.schmitz.processnotifierdemo.dto.ProcessEntityRequest;
import com.schmitz.processnotifierdemo.dto.ProcessEntityResponse;
import com.schmitz.processnotifierdemo.dto.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

// TODO: adjust to fit processing idea
// * we need a couple of things to make this fit the processing concept:
// ^ - rename things to better fit an entity procesor controller, messages, etc
// ^ - send the id of the entity being processed up so that when messages come back down the front end knows which row to update
// ^ - update the front end to affect the row associated with the response
// ^ - pull things out into appropriate services and stuff
// ^ - read up on how to do a decoupled broker

@Controller
public class MessageController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/process/entity")
    public void processEntity(ProcessEntityRequest request) throws InterruptedException {
        //        fakeProcesses("test");
    }

    //    @MessageMapping("/process/entities")
    //    public void processEntities(List<ProcessEntityRequest> request) {
    //        request.forEach(entity -> {
    //            try {
    //                fakeProcesses(entity.getName());
    //            } catch (InterruptedException e) {
    //                e.printStackTrace();
    //            }
    //        });
    //    }

    private void fakeProcesses(String from, String entityName) throws InterruptedException {
        Thread.sleep(2000);
        sendStatusUpdate(from, ResponseType.STAGE_1);
        Thread.sleep(1000);
        sendStatusUpdate(from, ResponseType.STAGE_2);
        Thread.sleep(2500);
        sendStatusUpdate(from, ResponseType.STAGE_3);
    }

    public void sendStatusUpdate(String from, ResponseType type) {
        simpMessagingTemplate.convertAndSend("/topic/messages", new ProcessEntityResponse("status-update", "test", type));
    }
}
