package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String username;

    @Column(
            nullable = false
    )
    private String password;

    @Column(
            nullable = false
    )
    private Boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(
            name = "role_id",
            nullable = false
    )
    private Role role;
}