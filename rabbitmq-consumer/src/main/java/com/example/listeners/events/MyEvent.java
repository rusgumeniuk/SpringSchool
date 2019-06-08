package com.example.listeners.events;

import com.example.messages.Message;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")

public abstract class MyEvent extends ApplicationEvent {
    private Message msg;

    public MyEvent(Object source, Message msg) {
        super(source);
        this.msg = msg;
    }

    public Message getMessage() {
        return msg;
    }
}
