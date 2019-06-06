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
    public List<Lesson> getLessons() {
        return lessonService.getAll().stream()                
                .collect(Collectors.toList());
    }

    @GetMapping("/{lessonId}")
    public Lesson getLesson(@PathVariable Integer lessonId) {
        return lessonService.getObjectById(lessonId);
    }

    @PostMapping
    public Lesson createLesson(@Valid @RequestBody Lesson newLesson) throws URISyntaxException {
        return lessonService.saveObject(newLesson);
    }

    @PutMapping("/{lessonId}")
    public Lesson updateLesson(@Valid @RequestBody Lesson updatedLesson, @PathVariable Integer lessonId) throws URISyntaxException {
        return lessonService.updateObject(updatedLesson, lessonId);
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