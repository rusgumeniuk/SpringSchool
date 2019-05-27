package com.example;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpMethod;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.sql.Timestamp;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class StudentMessage extends Message<Student>{
    public StudentMessage(String description, HttpMethod httpMethod, String statusCode, Timestamp dateTime, String error) {
        super(description, httpMethod, statusCode, dateTime, error);
    }

    public StudentMessage() {
    }
}
