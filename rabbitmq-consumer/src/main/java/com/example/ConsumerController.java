package com.example;

import com.example.listeners.events.*;
import com.example.messages.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ConsumerController {
    private List<SseEmitter> lsEmitters = new ArrayList<SseEmitter>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerController.class);

    @Autowired
    private MessageRepository msgRepo;

    @EventListener({StudentGroupTeacherEvent.class})
    public void handleStudentGroupTeacherEvt(StudentGroupTeacherEvent evt)
    {
        handleEvent(evt);
    }
    @EventListener({SystemEvent.class})
    public void handleSystemEvt(SystemEvent evt)
    {
       handleEvent(evt);
    }
    @EventListener({LesSubEvent.class})
    public void handleLesSubEvt(LesSubEvent evt)
    {
        handleEvent(evt);
    }
    @EventListener({ArchEvent.class})
    public void handleArchEvt(ArchEvent evt)
    {
        handleEvent(evt);
    }

    private void handleEvent(MyEvent event){
        System.out.println("EVENT RECEIVED: " + event.getMessage().getDescription());
        msgRepo.save(event.getMessage());

        List<SseEmitter> deadEmitters = new ArrayList<SseEmitter>();
        this.lsEmitters.forEach(emitter -> {
            try {
                emitter.send(event.getMessage());
            }
            catch (Exception e) {
                LOGGER.error("Error ",e);
                deadEmitters.add(emitter);
            }
        });

        this.lsEmitters.removeAll(deadEmitters);
    }
}
