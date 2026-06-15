package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voyageur_id", nullable = false)
    private User voyageur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_id", nullable = false)
    private LigneInterurbaine ligne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arret_depart_id", nullable = false)
    private VilleEtape arretDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arret_arrivee_id", nullable = false)
    private VilleEtape arretArrivee;

    // Horaire choisi pour le trajet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horaire_id")
    private HoraireInterurbain horaire;

    // Date du voyage choisie par le voyageur
    @Column
    private LocalDate dateTrajet;

    @Column(nullable = false)
    private LocalDateTime dateReservation;

    @Column(unique = true, length = 500)
    private String codeQR;

    @Column(unique = true, length = 10)
    private String codeTexte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypePaiement typePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutReservation statut;

    @Column
    private Double montant;

    public enum TypePaiement {
        BANKILY, MASRVI, SEDAD, CLICK, BAMIS, BIMBANK, BCIPAY
    }

    public enum StatutReservation {
        EN_ATTENTE, CONFIRME, UTILISE, ANNULE
    }
}