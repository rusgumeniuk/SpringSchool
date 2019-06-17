package com.example.controllers;

import com.example.messages.Message;
import com.example.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<Message> getMessages() {
        return messageService.getAll();
        
    }

    @GetMapping("/{messageId}")
    public Message getMessage(@PathVariable Long messageId) {
        return messageService.getObjectById(messageId);
    }
}
