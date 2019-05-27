package com.example;

import com.example.listeners.events.GroupEvent;
import com.example.listeners.events.StudentEvent;
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

    @EventListener({GroupEvent.class})
    public void handleGroupEvt(GroupEvent evt)
    {
        System.out.println("EVENT RECEIVED: " + evt.getMessage().getDescription());
        GroupMessage message = evt.getMessage();
        msgRepo.save(new Message(message.getDescription(), message.getOperationType(), message.getStatusCode(), message.getError()));
        List<SseEmitter> deadEmitters = new ArrayList<SseEmitter>();
        this.lsEmitters.forEach(emitter -> {
            try {
                emitter.send(message);
            }
            catch (Exception e) {
                LOGGER.error("Error ",e);
                deadEmitters.add(emitter);
            }
        });

        this.lsEmitters.removeAll(deadEmitters);
    }

    @EventListener({StudentEvent.class})
    public void handleStudentEvt(StudentEvent evt)
    {
        System.out.println("EVENT RECEIVED: " + evt.getMessage().getDescription());
        StudentMessage message = evt.getMessage();
        this.msgRepo.save(new Message(message.getDescription(), message.getOperationType(), message.getStatusCode(), message.getError()));

        List<SseEmitter> deadEmitters = new ArrayList<SseEmitter>();
        this.lsEmitters.forEach(emitter -> {
            try {
                emitter.send(message);
            }
            catch (Exception e) {
                LOGGER.error("Error ",e);
                deadEmitters.add(emitter);
            }
        });

        this.lsEmitters.removeAll(deadEmitters);
    }
}
