package com.example.assemblers;

import com.example.Building;
import com.example.controllers.BuildingController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class BuildingResourcesAssembler implements ResourceAssembler<Building, Resource<Building>> {
    @Override
    public Resource<Building> toResource(Building building) {
        return new Resource<>(
                building,
                linkTo(methodOn(BuildingController.class).getBuilding(building.getId())).withSelfRel(),
                linkTo(methodOn(BuildingController.class).getBuildings()).withRel("buildings")
        );
    }
}
