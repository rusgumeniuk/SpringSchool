package com.example.listeners.events;

import com.example.messages.Message;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class ArchEvent extends MyEvent {
    public ArchEvent(Object source, Message msg) {
        super(source, msg);
    }
}