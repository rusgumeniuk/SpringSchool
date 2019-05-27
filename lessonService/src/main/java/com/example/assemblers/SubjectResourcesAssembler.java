package com.example.assemblers;

import com.example.Subject;
import com.example.controllers.SubjectController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class SubjectResourcesAssembler implements ResourceAssembler<Subject, Resource<Subject>> {
    @Override
    public Resource<Subject> toResource(Subject subject) {
        return new Resource<>(
                subject,
                linkTo(methodOn(SubjectController.class).getSubject(subject.getId())).withSelfRel(),
                linkTo(methodOn(SubjectController.class).getSubjects()).withRel("subjects")
        );
    }
}