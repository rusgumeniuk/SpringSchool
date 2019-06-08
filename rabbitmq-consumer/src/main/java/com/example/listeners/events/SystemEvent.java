package com.example.listeners.events;

import com.example.messages.Message;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class SystemEvent extends MyEvent {
    public SystemEvent(Object source, Message msg) {
        super(source, msg);
    }
}
