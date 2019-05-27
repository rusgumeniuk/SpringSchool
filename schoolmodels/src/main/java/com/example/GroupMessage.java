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
public class GroupMessage extends Message<Group> {
    public GroupMessage(String description, HttpMethod httpMethod, String statusCode, Timestamp dateTime, String error) {
        super(description, httpMethod, statusCode, dateTime, error);
    }

    public GroupMessage() {
    }
}
