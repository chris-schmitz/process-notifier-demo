package com.schmitz.processnotifierdemo.service;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.factories.ProcessingCompleteResponseFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

// !! A note about the process methods !!
// | for this proof of concept we're using synchronous processing as a stand in for the async processes
// | of sending messages to a queue. That said, the pub/sub nature of the websockets still makes this whole
// | process asynchronous as far as the front end is concerned. The client isn't sitting there waiting for us
// | to respond, it handed off the message via websockets and even though we're synchronously cranking on it here
// | the client is free to do whatever it wants. Then when we message down from this service the front end gets
// | those messages via their subscriptions.
// | So, yeah it's sync on the server side but this is still a good example of how sync here doesn't necessarily mean
// | sync between the client and server like it does with regular http requests.
@Service
public class EntityProcessorService {

    private SimpMessagingTemplate messagingTemplate;
    private SomeFakeProcessor fakeProcessor;

    private ProcessingCompleteResponseFactory processingCompleteResponseFactory;
    private String messageTopic;

    public EntityProcessorService(SimpMessagingTemplate messagingTemplate, SomeFakeProcessor fakeProcessor, ProcessingCompleteResponseFactory processingCompleteResponseFactory) {
        this.messagingTemplate = messagingTemplate;
        this.fakeProcessor = fakeProcessor;
        this.processingCompleteResponseFactory = processingCompleteResponseFactory;
    }


    public void processEntity(Entity entity) throws InterruptedException {
        ArrayList<Entity> successful = new ArrayList<>();
        ArrayList<Entity> failed = new ArrayList<>();
        successful.add(entity);
        try {
            sendToProcessor(entity);
        } catch (InterruptedException e) {
            failed.add(entity);
        }
        sendProcessCompleteNotification(entity.getFrom(), successful, failed);
    }

    private void sendToProcessor(Entity entity) throws InterruptedException {
        fakeProcessor.process(entity);
    }

    private void sendProcessCompleteNotification(String username, ArrayList<Entity> successful, ArrayList<Entity> failed) {
        messagingTemplate.convertAndSendToUser(username, getMessageTopic(), processingCompleteResponseFactory.build(successful, failed));
    }


    public void processEntities(String from, ArrayList<Entity> entities) {
        ArrayList<Entity> successful = new ArrayList<Entity>();
        ArrayList<Entity> failed = new ArrayList<Entity>();

        for (Entity entity : entities) {
            try {
                sendToProcessor(entity);
                successful.add(entity);
            } catch (InterruptedException e) {
                failed.add(entity);
                e.printStackTrace();
            }
        }

        sendProcessCompleteNotification(from, successful, failed);
    }

    public String getMessageTopic() {
        return messageTopic;
    }

    public void setMessageTopic(String messageTopic) {
        this.messageTopic = messageTopic;
    }

}
