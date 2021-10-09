package com.schmitz.processnotifierdemo.controller;

import com.schmitz.processnotifierdemo.dto.Message;
import com.schmitz.processnotifierdemo.dto.Response;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public Response relayMessage(Message message) {
        return new Response(message.getContent(), "test");
    }
}
