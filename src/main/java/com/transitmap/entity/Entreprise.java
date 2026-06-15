package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant l'entreprise de transport d'un agent.
 * Créée automatiquement lors de l'approbation de la demande d'inscription.
 */
@Entity
@Table(name = "entreprises")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom commercial de l'entreprise */
    @Column(nullable = false, length = 200)
    private String nom;

    /** Numéro d'enregistrement commercial */
    @Column(unique = true, length = 100)
    private String numeroCommercial;

    /** Adresse du siège social */
    @Column(length = 300)
    private String adresse;

    /** Type de service offert */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypeService typeService;

    /** Codes de paiement par portefeuille électronique */
    @Column(length = 50)
    private String codeBankily;

    @Column(length = 50)
    private String codeMasrvi;

    @Column(length = 50)
    private String codeSedad;

    @Column(length = 50)
    private String codeClick;

    @Column(length = 50)
    private String codeBamis;

    @Column(length = 50)
    private String codeBimbank;

    @Column(length = 50)
    private String codeBciPay;

    /** Agent propriétaire de l'entreprise */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    /** Types de service disponibles */
    public enum TypeService {
        INTERURBAIN
    }
}