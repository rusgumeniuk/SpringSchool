package com.example.controllers;

import com.example.Group;
import com.example.Student;
import com.example.assemblers.GroupResourcesAssembler;
import com.example.assemblers.StudentResourcesAssembler;
import com.example.exceptions.StudentNotFoundException;
import com.example.services.GroupService;
import com.example.services.StudentService;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    GroupService groupService;
    private final StudentResourcesAssembler assembler;
    private final GroupResourcesAssembler groupAssembler;

    public StudentController(StudentResourcesAssembler assembler, GroupResourcesAssembler groupResourcesAssembler) {
        this.assembler = assembler;
        this.groupAssembler = groupResourcesAssembler;
    }

    @GetMapping
    public Collection<Student> getStudents() {
        return studentService.getAll().stream()
                .collect(Collectors.toList());
    }

    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable Integer studentId) {
        return studentService.getObjectById(studentId);
        //return ResponseEntity.ok(assembler.toResource(student));
    }

    @PostMapping
    public Student createStudent(@Valid @RequestBody Student newStudent) throws URISyntaxException {
        return studentService.saveObject(newStudent);
    }

    @PutMapping("/{studentId}")
    public Student updateStudent(@Valid @RequestBody Student updatedStudent, @PathVariable Integer studentId) throws URISyntaxException {
        return studentService.updateObject(updatedStudent, studentId);        
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer studentId) {
        try {
            changeStudentGroup(studentId, -2);
            studentService.deleteObject(studentId);
        } catch (StudentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            System.out.println("ATTENTION " + ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studentId}/group")
    public Group getGroupOfStudent(@PathVariable Integer studentId) {
        Student student = studentService.getObjectById(studentId);
        if (student.getGroup() == null)
            return null;
        return groupService.getObjectById(student.getGroup().getId());        
    }

    @PostMapping("/{studentId}/group/{newGroupId}")
    public List<Student> changeStudentGroup(@PathVariable Integer studentId, @PathVariable Integer newGroupId) {
        Student student = studentService.getObjectById(studentId);
        removeStudentFromGroup(student);
        return addStudentToGroup(student, newGroupId);
    }

    private void removeStudentFromGroup(Student student) {
        if (student.getGroup() != null) {
            Group studentGroup = groupService.getObjectById(student.getGroup().getId());
            studentGroup.removeStudent(student);
            studentGroup.setStudents(studentGroup.getStudents());
            groupService.saveObject(studentGroup);
            studentService.saveObject(student);
        }
    }

    private List<Student> addStudentToGroup(Student student, Integer groupId) {
        Student stud = studentService.getObjectById(student.getId());

        if (groupId == -2)
            return null;
        Group group = groupService.getObjectById(groupId);
        stud.setGroup(group);
        group.addStudent(stud);
        group.setStudents(group.getStudents());
        groupService.saveObject(group);
        studentService.saveObject(stud);
        return getStudentsOfGroup(groupId);
    }

    private List<Student> getStudentsOfGroup(Integer groupId) {
        var group = groupService.getObjectById(groupId);
        return group.getStudents()
                .stream()
                .collect(Collectors.toList());
    }
}