package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ligne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 30
    )
    private String numero;

    @Column(
            nullable = false,
            length = 150
    )
    private String nom;

    @Column(
            length = 500
    )
    private String description;
}