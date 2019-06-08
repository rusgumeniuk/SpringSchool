package com.example.assemblers;

import com.example.messages.Message;
import com.example.controllers.MessageController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
@Component
public class MessageResourcesAssembler implements ResourceAssembler<Message, Resource<Message>> {
    @Override
    public Resource<Message> toResource(Message message) {
        return new Resource<>(
                message,
                linkTo(methodOn(MessageController.class).getMessage(message.getId())).withSelfRel(),
                linkTo(methodOn(MessageController.class).getMessages()).withRel("messages")
        );
    }
}