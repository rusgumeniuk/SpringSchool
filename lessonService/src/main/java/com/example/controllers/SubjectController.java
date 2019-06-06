package com.example.controllers;

import com.example.Group;
import com.example.Subject;
import com.example.assemblers.GroupResourcesAssembler;
import com.example.assemblers.SubjectResourcesAssembler;
import com.example.exceptions.SubjectNotFoundException;
import com.example.services.GroupService;
import com.example.services.SubjectService;
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
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    GroupService groupService;
    private final SubjectResourcesAssembler assembler;


    public SubjectController(SubjectResourcesAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping
    public List<Subject> getSubjects() {
        return subjectService.getAll().stream()               
                .collect(Collectors.toList());
    }

    @GetMapping("/{subjectId}")
    public Subject getSubject(@PathVariable Integer subjectId) {
        return subjectService.getObjectById(subjectId);        
    }

    @PostMapping
    public Subject createSubject(@Valid @RequestBody Subject newSubject) throws URISyntaxException {
        return subjectService.saveObject(newSubject);
    }

    @PutMapping("/{subjectId}")
    public Subject updateSubject(@Valid @RequestBody Subject updatedSubject, @PathVariable Integer subjectId) throws URISyntaxException {
        return subjectService.updateObject(updatedSubject, subjectId);
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable Integer subjectId) {
        try {
            subjectService.deleteObject(subjectId);
        } catch (SubjectNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            System.out.println("ATTENTION " + ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}