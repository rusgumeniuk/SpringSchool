package com.example.controllers;

import com.example.Group;
import com.example.Teacher;
import com.example.Student;
import com.example.assemblers.GroupResourcesAssembler;
import com.example.assemblers.TeacherResourcesAssembler;
import com.example.exceptions.TeacherDontMentorThisGroup;
import com.example.exceptions.TeacherNotFoundException;
import com.example.services.GroupService;
import com.example.services.TeacherService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
@RequestMapping("/teachers")
public class TeacherController {


    @Autowired
    private TeacherService teacherService;
    @Autowired
    private GroupService groupService;

    private final TeacherResourcesAssembler assembler;
    private final GroupResourcesAssembler groupAssembler;

    public TeacherController(TeacherResourcesAssembler assembler, GroupResourcesAssembler groupAssembler) {
        this.assembler = assembler;
        this.groupAssembler = groupAssembler;
    }

    @GetMapping
    public Resources<Resource<Teacher>> getTeachers() {
        List<Resource<Teacher>> list = teacherService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(TeacherController.class).getTeachers()).withSelfRel()
        );
    }

    @GetMapping(value = "/{teacherId}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ResourceSupport> getTeacher(@PathVariable Integer teacherId) {
        Teacher teacher = teacherService.getObjectById(teacherId);
        return ResponseEntity.ok(assembler.toResource(teacher));
    }

    @PostMapping
    public ResponseEntity<?> createTeacher(@Valid @RequestBody Teacher newTeacher) throws URISyntaxException {
        Resource<Teacher> resource = assembler.toResource(teacherService.saveObject(newTeacher));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping(value = "/{teacherId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> updateTeacher(@Valid @RequestBody Teacher updatedTeacher, @PathVariable Integer teacherId) throws URISyntaxException {
        Teacher updatedObj = teacherService.updateObject(updatedTeacher, teacherId);

        Resource<Teacher> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{teacherId}/delall")
    public ResponseEntity<?> deleteTeacherAndMentoredGroup(@PathVariable Integer teacherId) {
        try{
            teacherService.deleteObject(teacherId);
        }
        catch (TeacherNotFoundException ex){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<?> deleteTeacherAndSaveMentoredGroup(@PathVariable Integer teacherId) {
        Teacher teacher = teacherService.getObjectById(teacherId);
        if(teacher.getMentored_group() != null){
            Group mentoredGroup = groupService.getObjectById(teacher.getMentored_group().getId());
            removeGroupFromMentor(teacherId, teacher.getMentored_group().getId());
        }
        return deleteTeacherAndMentoredGroup(teacherId);
    }

    @GetMapping("/{teacherId}/group")
    public ResponseEntity<Resource<Group>> getMentoredGroupOfTeacher(@PathVariable Integer teacherId) {
        var teacher = teacherService.getObjectById(teacherId);
        Group group = teacher.getMentored_group();
        return ResponseEntity.ok(groupAssembler.toResource(group));
    }


    @DeleteMapping("/{teacherId}/group/{mentoredGroupId}")
    public ResponseEntity<Resource<Group>> removeGroupFromMentor(@PathVariable Integer teacherId, @PathVariable Integer mentoredGroupId) {
        Group group = groupService.getObjectById(mentoredGroupId);
        Teacher teacher = teacherService.getObjectById(teacherId);
        if (!(teacher.getMentored_group().getId() == group.getId()))
            throw new TeacherDontMentorThisGroup(teacher, group);
        else {
            //group.setMentor(teacher);
            teacher.setMentored_group(group);
            teacherService.saveObject(teacher);
            groupService.saveObject(group);
        }
        return getMentoredGroupOfTeacher(teacherId);
    }

    @PostMapping("/{teacherId}/group/{mentoredGroupId}")
    public ResponseEntity<Resource<Group>> addGroupToMentor(@PathVariable Integer teacherId, @PathVariable Integer mentoredGroupId) {
        Group group = groupService.getObjectById(mentoredGroupId);
        Teacher teacher = teacherService.getObjectById(teacherId);
        teacher.setMentored_group(group);
        group.setMentor(teacher);
        teacherService.saveObject(teacher);
        groupService.saveObject(group);
        return getMentoredGroupOfTeacher(teacherId);
    }
}
