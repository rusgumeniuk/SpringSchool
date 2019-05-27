package com.example.listeners.events;

import com.example.messages.StudentMessage;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class StudentEvent extends ApplicationEvent {

    private StudentMessage msg;

    public StudentEvent(Object source, StudentMessage msg) {
        super(source);
        this.msg = msg;
    }

    public StudentMessage getMessage() {
        return msg;
    }

}
