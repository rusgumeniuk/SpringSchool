package com.example.security;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Data
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    private String username;
    private String password;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    private Boolean enabled;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
