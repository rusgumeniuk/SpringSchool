package com.example.listeners.events;

import com.example.messages.Message;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class LesSubEvent extends MyEvent {
    public LesSubEvent(Object source, Message msg) {
        super(source, msg);
    }
}