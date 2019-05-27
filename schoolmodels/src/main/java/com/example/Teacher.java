package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "teachers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Teacher extends com.example.Entity {

    @NotBlank
    private String fullName;

    @NotNull
    private TeacherRank teacherRank;

    @NotBlank
    private String cathedra;

    @OneToOne(cascade = CascadeType.ALL)
    private Group mentored_group;


    public Teacher() {}

    public Teacher(@NotBlank String fullName, @NotBlank TeacherRank teacherRank, @NotBlank String cathedra) {
        this.fullName = fullName;
        this.teacherRank = teacherRank;
        this.cathedra = cathedra;
    }
    public Teacher(@NotBlank String fullName, @NotBlank TeacherRank teacherRank, @NotBlank String cathedra, Group mentored_group) {
        this.fullName = fullName;
        this.teacherRank = teacherRank;
        this.cathedra = cathedra;
        this.mentored_group = mentored_group;
    }

    public void setMentored_group(Group mentored_group) {
        if(mentored_group != null)
            deleteMentoring();
        this.mentored_group = mentored_group;
    }
    public void deleteMentoring(){
        if(mentored_group != null){
            mentored_group.setMentor(null);
            mentored_group = null;
        }
    }
}
