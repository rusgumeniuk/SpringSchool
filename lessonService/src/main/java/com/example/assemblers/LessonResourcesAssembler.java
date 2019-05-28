package com.example.assemblers;

import com.example.controllers.LessonController;
import com.example.lessons.Lesson;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class LessonResourcesAssembler implements ResourceAssembler<Lesson, Resource<Lesson>> {
    @Override
    public Resource<Lesson> toResource(Lesson lesson) {
        return new Resource<>(
                lesson,
                linkTo(methodOn(LessonController.class).getLesson(lesson.getId())).withSelfRel(),
                linkTo(methodOn(LessonController.class).getLessons()).withRel("lessons")
        );
    }
}
