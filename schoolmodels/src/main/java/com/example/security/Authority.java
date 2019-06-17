package com.example.security;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Data
@Table(name="authorities")
@EntityListeners(AuditingEntityListener.class)
public class Authority {

    @Id
    private String username;
    private String authority;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


    public Authority(String username, String authority){
        this.username = username;
        this.authority = authority;
    }

    public Authority(){

    }

}

