package com.example;

import com.example.messages.Message;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class MsgProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgProducer.class);

    public void sendMessage(Message message){
        RabbitTemplate rabbitTemplate = null;
        if(message.getClassName().equalsIgnoreCase("student")
                || message.getClassName().equalsIgnoreCase("group")
                || message.getClassName().equalsIgnoreCase("teacher"))
            rabbitTemplate = rabbitStudentGroupTeacher;
        else
            if(message.getClassName().equalsIgnoreCase("subject")
                    || message.getClassName().equalsIgnoreCase("lesson"))
            rabbitTemplate = rabbitLesSub;
        else
            if(message.getClassName().equalsIgnoreCase("room")
                    || message.getClassName().equalsIgnoreCase("building"))
            rabbitTemplate = rabbitArch;
        else
            rabbitTemplate = rabbitSystem;
        sendMessage(rabbitTemplate, message);
    }

    private void sendMessage(RabbitTemplate rabbitTemplate, Message message){
        try {
            LOGGER.debug("<<<<< SENDING MESSAGE");
            rabbitTemplate.convertAndSend(message);
            LOGGER.debug(MessageFormat.format("MESSAGE SENT TO {0} >>>>>>", rabbitTemplate.getRoutingKey()));
        } catch (AmqpException e) {
            LOGGER.error("Error sending " + message.getClassName() + " message : " + e);
        }
    }

    public ObjectNode info()
    {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = factory.objectNode();
        root.put("host", rabbitSystem.getConnectionFactory().getHost());
        root.put("port", rabbitSystem.getConnectionFactory().getPort());

        root.put("Student Group Teacher UUID", rabbitStudentGroupTeacher.getUUID());
        root.put("Room and Building UUID", rabbitArch.getUUID());
        root.put("Lesson and Subject UUID", rabbitLesSub.getUUID());
        root.put("System UUID", rabbitSystem.getUUID());

        root.put("queueStudentGroupTeacher", rabbitStudentGroupTeacher.getRoutingKey());
        root.put("queueArch", rabbitArch.getRoutingKey());
        root.put("queueLesSub", rabbitLesSub.getRoutingKey());
        root.put("queueSystem", rabbitSystem.getRoutingKey());
        return root;
    }

    @Autowired
    @Qualifier("rabbitTemplateStudentGroupTeacher")
    private RabbitTemplate rabbitStudentGroupTeacher;
    @Autowired
    @Qualifier("rabbitTemplateArch")
    private RabbitTemplate rabbitArch;
    @Autowired
    @Qualifier("rabbitTemplateSystem")
    private RabbitTemplate rabbitSystem;
    @Autowired
    @Qualifier("rabbitTemplateLesSub")
    private RabbitTemplate rabbitLesSub;
}
