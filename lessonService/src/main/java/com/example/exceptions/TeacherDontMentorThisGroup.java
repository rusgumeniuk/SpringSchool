package com.example.exceptions;

import com.example.Group;
import com.example.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TeacherDontMentorThisGroup extends RuntimeException {
    public TeacherDontMentorThisGroup(Teacher teacher, Group group){
        super("Teacher :" + teacher.getId() + " doesn't mentor group :" + group.getId());
    }

}
