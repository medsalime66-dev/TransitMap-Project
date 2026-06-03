package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entité représentant un paiement électronique.
 * Lié à une réservation après confirmation du code de transaction.
 */
@Entity
@Table(name = "paiements")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Réservation associée à ce paiement */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    /** Portefeuille électronique utilisé */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MethodePaiement methode;

    /** Montant payé en MRU */
    @Column(nullable = false)
    private Double montant;

    /** Code de transaction saisi par le voyageur */
    @Column(length = 100)
    private String codeTransaction;

    /** Date de validation du paiement */
    @Column
    private LocalDateTime dateValidation;

    /** Statut du paiement */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutPaiement statut;

    /** Portefeuilles électroniques disponibles */
    public enum MethodePaiement {
        BANKILY, MASRVI, SEDAD, CLICK, BAMIS, BIMBANK, BCIPAY
    }

    /** Statuts possibles d'un paiement */
    public enum StatutPaiement {
        EN_ATTENTE, VALIDE, ECHEC
    }
}