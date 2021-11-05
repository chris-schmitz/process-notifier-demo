package com.schmitz.processnotifierdemo.controller;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessEntityRequest;
import com.schmitz.processnotifierdemo.factories.EntityFactory;
import com.schmitz.processnotifierdemo.service.EntityProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
    EntityFactory entityFactory;

    @Autowired
    EntityProcessorService processorService;

    // TODO: replace and add some kind of authenticated user setup
    @MessageMapping("/process/entity")
    public void processEntity(ProcessEntityRequest request) throws InterruptedException {
        Entity entity = entityFactory.build("some user", request.getName());

        processorService.processEntity(entity);
    }

    //public void sendStatusUpdate(String from, ResponseType type) {
    //    simpMessagingTemplate.convertAndSend("/topic/messages", new ProcessEntityResponse("status-update", "test", type));
    //}
}
