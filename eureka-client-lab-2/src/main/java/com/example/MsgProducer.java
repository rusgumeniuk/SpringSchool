package com.example;

import com.example.messages.GroupMessage;
import com.example.messages.StudentMessage;
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

    @Autowired
    @Qualifier("rabbitTemplateGroup")
    private RabbitTemplate rabbitGroup;

    @Autowired
    @Qualifier("rabbitTemplateStudent")
    private RabbitTemplate rabbitStudent;

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgProducer.class);

    public void sendGroupMsg(GroupMessage msg)
    {
        try {
            LOGGER.debug("<<<<<< SENDING MESSAGE");
            rabbitGroup.convertAndSend(msg);
            LOGGER.debug(MessageFormat.format("MESSAGE SENT TO {0} >>>>>>", rabbitGroup.getRoutingKey()));

        } catch (AmqpException e) {
            LOGGER.error("Error sending Customer: ",e);
        }
    }

    public void sendStudentMsg(StudentMessage msg)
    {
        try {
            LOGGER.debug("<<<<< SENDING MESSAGE");
            rabbitStudent.convertAndSend(msg);
            LOGGER.debug(MessageFormat.format("MESSAGE SENT TO {0} >>>>>>", rabbitStudent.getRoutingKey()));
        } catch (AmqpException e) {
            LOGGER.error("Error sending Shop: ",e);
        }
    }

    public ObjectNode info()
    {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = factory.objectNode();
        root.put("host", rabbitGroup.getConnectionFactory().getHost());
        root.put("port", rabbitGroup.getConnectionFactory().getPort());
        root.put("Group UUID", rabbitGroup.getUUID());
        root.put("Student UUID", rabbitStudent.getUUID());
        root.put("queueGroup", rabbitGroup.getRoutingKey());
        root.put("queueStudent", rabbitStudent.getRoutingKey());

        return root;
    }
}
