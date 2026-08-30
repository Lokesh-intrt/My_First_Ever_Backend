package com.example.project1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(force = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false , unique = true)
    @Email
    private String email;

    @Column(nullable = false )
    private String password;

    public enum Roles{
        ROLE_USER,
        ROLE_ADMIN,
        ROLE_SELLER,
        ROLE_REVIEWER
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Roles role;

}
