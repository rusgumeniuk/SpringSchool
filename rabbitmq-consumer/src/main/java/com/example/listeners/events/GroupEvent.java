package com.example.listeners.events;

import com.example.messages.GroupMessage;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class GroupEvent extends ApplicationEvent {

    private GroupMessage msg;

    public GroupEvent(Object source, GroupMessage msg) {
        super(source);
        this.msg = msg;
    }

    public GroupMessage getMessage() {
        return msg;
    }



}
