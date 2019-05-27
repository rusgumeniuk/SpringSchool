package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "students")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Teacher extends com.example.Entity {

    @NotBlank
    private String fullName;

    @NotBlank
    private TeacherRank teacherRank;

    @NotBlank
    private String cathedra;

    @JoinColumn(name = "group_id")
    private Group mentored_group;

    public Teacher() {}

    public Teacher(@NotBlank String fullName, @NotBlank TeacherRank teacherRank, @NotBlank String cathedra, Group mentored_group) {
        this.fullName = fullName;
        this.teacherRank = teacherRank;
        this.cathedra = cathedra;
        this.mentored_group = mentored_group;
    }
}
