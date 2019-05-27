package com.example.assemblers;

import com.example.Student;
import com.example.controllers.StudentController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class StudentResourcesAssembler implements ResourceAssembler<Student, Resource<Student>> {
    @Override
    public Resource<Student> toResource(Student student) {
        return new Resource<>(
                student,
                linkTo(methodOn(StudentController.class).getStudent(student.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).getStudents()).withRel("students")
        );
    }
}
