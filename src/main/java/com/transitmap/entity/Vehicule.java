package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant un véhicule de transport.
 * Un véhicule appartient à une ligne et peut être assigné à un chauffeur.
 */
@Entity
@Table(name = "vehicules")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Numéro d'immatriculation unique du véhicule */
    @Column(nullable = false, unique = true, length = 50)
    private String matricule;

    /** Marque et modèle du véhicule */
    @Column(length = 100)
    private String marque;

    /** Capacité totale en nombre de places */
    @Column(nullable = false)
    private Integer capacite;

    /** Nombre de places actuellement disponibles */
    @Column(nullable = false)
    private Integer placesDisponibles;

    /** Année de fabrication */
    @Column
    private Integer annee;

    /** Statut opérationnel du véhicule */
    @Column(nullable = false, length = 50)
    private String statut;

    /** Position GPS actuelle (latitude) */
    private Double latitudeActuelle;

    /** Position GPS actuelle (longitude) */
    private Double longitudeActuelle;

    /** Ligne à laquelle le véhicule est assigné */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_id", nullable = false)
    private Ligne ligne;

    /** Chauffeur assigné (optionnel) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chauffeur_id")
    private Chauffeur chauffeur;
}