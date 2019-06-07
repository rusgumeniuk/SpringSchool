package com.example.services;

import com.example.exceptions.MessageNotFoundException;
import com.example.messages.Message;
import com.example.messages.MessageRepository;
import lombok.experimental.var;
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
        var message = repository.findById(aLong);
        if(message.isPresent()){
            return message.get();
        }
        throw new MessageNotFoundException(aLong);
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
