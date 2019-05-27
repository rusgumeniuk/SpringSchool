package com.example.controllers;

import com.example.Message;
import com.example.assemblers.MessageResourcesAssembler;
import com.example.services.MessageService;
import com.example.exceptions.*;
import com.example.services.GroupService;
import com.example.services.MessageService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
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
    public Resources<Resource<Message>> getMessages() {
        List<Resource<Message>> list = messageService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(MessageController.class).getMessages()).withSelfRel()
        );
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<ResourceSupport> getMessage(@PathVariable Long messageId) {
        var message = messageService.getObjectById(messageId);
        return ResponseEntity.ok(assembler.toResource(message));
    }
}
