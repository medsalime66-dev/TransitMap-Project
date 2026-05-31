package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lignes_interurbaines")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LigneInterurbaine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String villeDepart;

    @Column(nullable = false, length = 100)
    private String villeArrivee;

    @Column(nullable = false)
    private Double distanceKm;

    @Column(nullable = false)
    private Double prixBase;

    @Column(length = 500)
    private String description;
}