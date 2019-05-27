package com.example.assemblers;

import com.example.Teacher;
import com.example.controllers.TeacherController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import javax.persistence.Column;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TeacherResourcesAssembler implements ResourceAssembler<Teacher, Resource<Teacher>> {
    @Override
    public Resource<Teacher> toResource(Teacher teacher) {
        return new Resource<>(
                teacher,
                linkTo(methodOn(TeacherController.class).getTeacher(teacher.getId())).withSelfRel(),
                linkTo(methodOn(TeacherController.class).getTeachers()).withRel("teachers")
        );
    }
}
