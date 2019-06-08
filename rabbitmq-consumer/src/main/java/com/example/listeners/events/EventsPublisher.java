package com.example.listeners.events;

import com.example.messages.Message;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class EventsPublisher implements ApplicationEventPublisherAware {

    protected ApplicationEventPublisher appPublisher;

    @Override
    public void setApplicationEventPublisher(final ApplicationEventPublisher appPublisher) {

        this.appPublisher = appPublisher;
    }

    public void publishStudentGroupTeacher(Message message)
    {
        StudentGroupTeacherEvent evt = new StudentGroupTeacherEvent(this, message);
        appPublisher.publishEvent(evt);
    }

    public void publishSystem(Message message)
    {
        SystemEvent evt = new SystemEvent(this, message);
        appPublisher.publishEvent(evt);
    }
    public void publishArch(Message message)
    {
        ArchEvent evt = new ArchEvent(this, message);
        appPublisher.publishEvent(evt);
    }

    public void publishLesSub(Message message)
    {
        LesSubEvent evt = new LesSubEvent(this, message);
        appPublisher.publishEvent(evt);
    }
}
