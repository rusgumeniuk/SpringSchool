package com.example.controllers;

import com.example.Subject;
import com.example.exceptions.SubjectNotFoundException;
import com.example.services.GroupService;
import com.example.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    GroupService groupService;

    @GetMapping
    public List<Subject> getSubjects() {
        return subjectService.getAll();
    }

    @GetMapping("/{subjectId}")
    public Subject getSubject(@PathVariable Integer subjectId) {
        return subjectService.getObjectById(subjectId);        
    }

    @PostMapping
    public Subject createSubject(@Valid @RequestBody Subject newSubject){
        return subjectService.saveObject(newSubject);
    }

    @PutMapping("/{subjectId}")
    public Subject updateSubject(@Valid @RequestBody Subject updatedSubject, @PathVariable Integer subjectId){
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