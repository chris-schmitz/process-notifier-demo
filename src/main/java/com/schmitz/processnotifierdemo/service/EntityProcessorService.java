package com.schmitz.processnotifierdemo.service;

import com.schmitz.processnotifierdemo.dto.Entity;
import com.schmitz.processnotifierdemo.dto.ProcessingCompleteResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
public class EntityProcessorService {

    private SimpMessagingTemplate messagingTemplate;
    private SomeFakeProcessor fakeProcessor;
    private ProcessingCompleteResponse response;
    private String messageTopic;

    public EntityProcessorService(SimpMessagingTemplate messagingTemplate, SomeFakeProcessor fakeProcessor, ProcessingCompleteResponse response) {
        this.messagingTemplate = messagingTemplate;
        this.fakeProcessor = fakeProcessor;
        this.response = response;
    }


    public void processEntity(Entity entity) throws InterruptedException {
        fakeProcessor.process(entity);
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(entity);
        messagingTemplate.convertAndSendToUser(entity.getFrom(), getMessageTopic(), response.generate(entities));
    }

    public String getMessageTopic() {
        return messageTopic;
    }

    public void setMessageTopic(String messageTopic) {
        this.messageTopic = messageTopic;
    }

    public void processEntities(String from, ArrayList<Entity> entities) {
        entities.forEach(entity -> {
            try {
                fakeProcessor.process(entity);
            } catch (InterruptedException e) {
                // Todo: start from here
                // ? hmmmmmmmmmmm what do we want to do here??
                // ? really, really we should make a list of failed processes and then if it's not empty
                // ? message down to the client with a report of what worked and what didn't.
                // ? what do you think, future me? ;P
                e.printStackTrace();
            }
        });


    }
}
