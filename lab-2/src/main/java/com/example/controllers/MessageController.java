package com.example.controllers;

import com.example.messages.Message;
import com.example.assemblers.MessageResourcesAssembler;
import com.example.services.MessageService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;
    private final MessageResourcesAssembler assembler;
    public MessageController(MessageResourcesAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping
    public List<Message> getMessages() {
        return messageService.getAll().stream()               
                .collect(Collectors.toList());
        
    }

    @GetMapping("/{messageId}")
    public Message getMessage(@PathVariable Long messageId) {
        return messageService.getObjectById(messageId);
    }
}
