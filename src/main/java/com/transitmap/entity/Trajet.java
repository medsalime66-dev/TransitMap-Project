package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entité représentant un trajet planifié sur une ligne.
 * Un trajet est associé à un véhicule et un chauffeur.
 */
@Entity
@Table(name = "trajets")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Date du trajet */
    @Column(nullable = false)
    private LocalDate dateTrajet;

    /** Heure de départ prévue */
    @Column(nullable = false)
    private LocalTime heureDepart;

    /** Heure d'arrivée prévue */
    @Column(nullable = false)
    private LocalTime heureArrivee;

    /** Statut du trajet */
    @Column(nullable = false, length = 50)
    private String statut;

    /** Ligne sur laquelle se déroule le trajet */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_id", nullable = false)
    private Ligne ligne;

    /** Véhicule assigné à ce trajet */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    /** Chauffeur assigné à ce trajet */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chauffeur_id")
    private Chauffeur chauffeur;
}