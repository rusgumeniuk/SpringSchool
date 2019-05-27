package com.example.listeners.impl;

import com.example.messages.GroupMessage;
import com.example.listeners.RabbitListener;
import com.example.listeners.events.EventsPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListenerGroup implements RabbitListener<GroupMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerGroup.class);

    @Autowired
    private EventsPublisher publisher;

    @Override
    public void receiveMessage(GroupMessage message) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
            LOGGER.debug("Receive Message Group: \n"+json);

            publisher.publishGroup(message);

        } catch (JsonProcessingException e) {
            LOGGER.error("Error: ", e);
        }

    }
}
