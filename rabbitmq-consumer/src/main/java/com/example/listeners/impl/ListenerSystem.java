package com.example.listeners.impl;

import com.example.listeners.RabbitListener;
import com.example.listeners.events.EventsPublisher;
import com.example.messages.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListenerSystem implements RabbitListener<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerSystem.class);

    @Autowired
    private EventsPublisher publisher;

    @Override
    public void receiveMessage(Message message) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
            LOGGER.debug("Receive Message system: \n"+json);

            publisher.publishSystem(message);

        } catch (JsonProcessingException e) {
            LOGGER.error("Error: ", e);
        }

    }



}
