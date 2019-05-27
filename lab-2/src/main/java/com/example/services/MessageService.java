package com.example.services;

import com.example.Message;
import com.example.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class MessageService implements Service<Message, Long> {
    @Autowired
    MessageRepository repository;
    @Override
    public List<Message> getAll() {
        return repository.findAll();
    }

    @Override
    public Message getObjectById(Long aLong) {
        return null;
    }

    @Override
    public Message saveObject(Message newObject) {
        return null;
    }

    @Override
    public void deleteObject(Long aLong) {

    }

    @Override
    public Message updateObject(Message newObject, Long aLong) {
        return null;
    }
}
