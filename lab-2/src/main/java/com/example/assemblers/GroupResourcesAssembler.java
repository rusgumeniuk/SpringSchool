package com.example.assemblers;


import com.example.Group;
import com.example.controllers.GroupController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class GroupResourcesAssembler implements ResourceAssembler<Group, Resource<Group>> {
    @Override
    public Resource<Group> toResource(Group group) {
        return new Resource<>(
                group,
                linkTo(methodOn(GroupController.class).getGroup(group.getId())).withSelfRel(),
                linkTo(methodOn(GroupController.class).getGroups()).withRel("groups")
        );
    }
}
