package com.backend.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
@Table(name="user", schema = "prod")
public class User {

    @Id
    private String id;
    private String email;
    private String username;

    @OneToMany(mappedBy = "user")
    private List<Portfolio> portfolios;

    protected User() {
    }

    public User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
