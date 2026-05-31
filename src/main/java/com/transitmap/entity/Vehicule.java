package com.transitmap.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "vehicules")

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String matricule;

    @Column(
            nullable = false
    )
    private Integer capacite;

    @Column(
            nullable = false,
            length = 50
    )
    private String statut;

    private Double currentLatitude;

    private Double currentLongitude;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(
            name = "ligne_id",
            nullable = false
    )
    private Ligne ligne;
}