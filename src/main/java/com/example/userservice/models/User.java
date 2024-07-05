package com.example.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseModel {
    private String name;
    private String email;
    private String hashedPassword;
    @ManyToMany
    private List<Role> roles;
    private boolean isEmailVerified;
}
