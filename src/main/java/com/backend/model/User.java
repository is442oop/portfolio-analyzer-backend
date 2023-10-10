package com.backend.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GenerationType;

import lombok.Data;

@Data
@Entity
@Table(name="user", schema = "prod")
@SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq") // convention: `{TABLE}_{COLUMN}_seq`
    private Long id;
    private String email;
    private String username;

    protected User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
