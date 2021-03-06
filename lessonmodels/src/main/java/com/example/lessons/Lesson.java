package com.example.lessons;
import com.example.Group;
import com.example.Room;
import com.example.Subject;
import com.example.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;


@Data
@Entity
@Table(name = "lessons")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)

public class Lesson extends com.example.Entity {

    @NotNull
    private LessonNumber lessonNumber;
    @NotNull
    private LessonType lessonType;
    @NotNull
    private WeekMode weekMode;
    @NotNull
    private DayOfWeek dayOfWeek;

    @NotBlank
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Room room;

    @NotBlank
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Subject subject;

    @NotBlank
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Teacher teacher;

    @NotBlank
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Group group;

    public Lesson() {}
    public Lesson(@NotBlank Room room, @NotBlank Subject subject, @NotBlank Teacher teacher, @NotBlank Group group) {
        this.room = room;
        this.subject = subject;
        this.teacher = teacher;
        this.group = group;

        weekMode = WeekMode.BOTH;
        dayOfWeek = DayOfWeek.MONDAY;
        lessonNumber = LessonNumber.THIRD;
        lessonType = LessonType.LECTURE;
    }
    public Lesson(@NotNull LessonNumber lessonNumber, @NotNull LessonType lessonType, @NotNull WeekMode weekMode, @NotNull DayOfWeek dayOfWeek, @NotBlank Room room, @NotBlank Subject subject, @NotBlank Teacher teacher, @NotBlank Group group) {
        this.lessonNumber = lessonNumber;
        this.lessonType = lessonType;
        this.weekMode = weekMode;
        this.dayOfWeek = dayOfWeek;
        this.room = room;
        this.subject = subject;
        this.teacher = teacher;
        this.group = group;
    }
}


