package com.example.listeners.events;

import com.example.GroupMessage;
import com.example.StudentMessage;
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

    public void publishGroup(GroupMessage message)
    {
        GroupEvent evt = new GroupEvent(this, message);
        appPublisher.publishEvent(evt);
    }

    public void publishStudent(StudentMessage message)
    {
        StudentEvent evt = new StudentEvent(this, message);
        appPublisher.publishEvent(evt);
    }
}
