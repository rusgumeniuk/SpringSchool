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
    public Resources<Resource<Subject>> getSubjects() {
        List<Resource<Subject>> list = subjectService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(SubjectController.class).getSubjects()).withSelfRel()
        );
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<ResourceSupport> getSubject(@PathVariable Integer subjectId) {
        var subject = subjectService.getObjectById(subjectId);
        return ResponseEntity.ok(assembler.toResource(subject));
    }

    @PostMapping
    public ResponseEntity<?> createSubject(@Valid @RequestBody Subject newSubject) throws URISyntaxException {
        Resource<Subject> resource = assembler.toResource(subjectService.saveObject(newSubject));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<?> updateSubject(@Valid @RequestBody Subject updatedSubject, @PathVariable Integer subjectId) throws URISyntaxException {
        Subject updatedObj = subjectService.updateObject(updatedSubject, subjectId);
        Resource<Subject> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
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