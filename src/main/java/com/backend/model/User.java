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
@SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
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
