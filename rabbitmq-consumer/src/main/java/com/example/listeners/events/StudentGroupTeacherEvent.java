package com.example.listeners.events;

import com.example.messages.Message;
import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class StudentGroupTeacherEvent extends MyEvent {
    public StudentGroupTeacherEvent(Object source, Message msg) {
        super(source, msg);
    }
}
