package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une réservation effectuée par un voyageur.
 * Contient le QR code et le code textuel pour validation.
 */
@Entity
@Table(name = "reservations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Voyageur ayant effectué la réservation */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voyageur_id", nullable = false)
    private User voyageur;

    /** Trajet réservé */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    /** Arrêt de départ choisi par le voyageur */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arret_depart_id", nullable = false)
    private Arret arretDepart;

    /** Arrêt d'arrivée choisi par le voyageur */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arret_arrivee_id", nullable = false)
    private Arret arretArrivee;

    /** Date et heure de la réservation */
    @Column(nullable = false)
    private LocalDateTime dateReservation;

    /** QR code sous forme de chaîne encodée */
    @Column(unique = true, length = 500)
    private String codeQR;

    /** Code textuel court pour validation manuelle */
    @Column(unique = true, length = 10)
    private String codeTexte;

    /** Type de paiement utilisé */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypePaiement typePaiement;

    /** Statut actuel de la réservation */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutReservation statut;

    /** Montant payé */
    @Column
    private Double montant;

    /** Types de paiement acceptés */
    public enum TypePaiement {
        ELECTRONIQUE, CASH
    }

    /** Statuts possibles d'une réservation */
    public enum StatutReservation {
        EN_ATTENTE, CONFIRME, UTILISE, ANNULE
    }
}