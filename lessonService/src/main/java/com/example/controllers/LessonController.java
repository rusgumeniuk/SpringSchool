package com.example.controllers;

import com.example.assemblers.LessonResourcesAssembler;
import com.example.exceptions.LessonNotFoundException;
import com.example.lessons.Lesson;
import com.example.services.GroupService;
import com.example.services.LessonService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    GroupService groupService;
    private final LessonResourcesAssembler assembler;


    public LessonController(LessonResourcesAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping
    public Resources<Resource<Lesson>> getLessons() {
        List<Resource<Lesson>> list = lessonService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(LessonController.class).getLessons()).withSelfRel()
        );
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<ResourceSupport> getLesson(@PathVariable Integer lessonId) {
        var lesson = lessonService.getObjectById(lessonId);
        return ResponseEntity.ok(assembler.toResource(lesson));
    }

    @PostMapping
    public ResponseEntity<?> createLesson(@Valid @RequestBody Lesson newLesson) throws URISyntaxException {
        Resource<Lesson> resource = assembler.toResource(lessonService.saveObject(newLesson));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<?> updateLesson(@Valid @RequestBody Lesson updatedLesson, @PathVariable Integer lessonId) throws URISyntaxException {
        Lesson updatedObj = lessonService.updateObject(updatedLesson, lessonId);
        Resource<Lesson> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable Integer lessonId) {
        try {
            lessonService.deleteObject(lessonId);
        } catch (LessonNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            System.out.println("ATTENTION " + ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}