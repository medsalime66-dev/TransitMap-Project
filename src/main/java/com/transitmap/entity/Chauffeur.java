package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant un chauffeur de bus.
 * Un chauffeur est créé par un agent et peut être assigné à un véhicule.
 */
@Entity
@Table(name = "chauffeurs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Chauffeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom complet du chauffeur */
    @Column(nullable = false, length = 150)
    private String nomComplet;

    /** Numéro de permis de conduire */
    @Column(nullable = false, unique = true, length = 50)
    private String numeroPermis;

    /** Numéro de téléphone */
    @Column(nullable = false, length = 20)
    private String telephone;

    /** Email utilisé pour la connexion */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /** Compte utilisateur associé pour la connexion */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** Agent qui a créé ce chauffeur */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    /** Ligne assignée (optionnel) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_id")
    private Ligne ligne;
}