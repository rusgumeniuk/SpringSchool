package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "subjects")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Subject extends com.example.Entity {

    @NotBlank
    private String title;

    @NotNull
    private ControlType controlType;

    public Subject(@NotBlank String title, @NotNull ControlType controlType) {
        this.title = title;
        this.controlType = controlType;
    }
    public Subject(){}
}
