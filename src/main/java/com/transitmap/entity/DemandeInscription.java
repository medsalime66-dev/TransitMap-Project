package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entité représentant une demande d'inscription d'un agent.
 * Soumise avant la création du compte, traitée par l'administrateur.
 */
@Entity
@Table(name = "demandes_inscription")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DemandeInscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // === Informations personnelles ===

    /** Nom complet du demandeur */
    @Column(nullable = false, length = 150)
    private String nomComplet;

    /** Email du demandeur */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /** Numéro de téléphone */
    @Column(nullable = false, length = 20)
    private String telephone;

    /** Numéro de la carte d'identité nationale */
    @Column(length = 50)
    private String numeroCIN;

    // === Informations entreprise ===

    /** Nom de l'entreprise */
    @Column(nullable = false, length = 200)
    private String nomEntreprise;

    /** Numéro d'enregistrement commercial */
    @Column(length = 100)
    private String numeroCommercial;

    /** Adresse du siège social */
    @Column(length = 300)
    private String adresse;

    /** Type de service souhaité */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Entreprise.TypeService typeService;

    /** Description / message de la demande */
    @Column(length = 1000)
    private String description;

    // === Codes de paiement (au moins un obligatoire) ===

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

    /** Statut de la demande */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutDemande statut;

    /** Date de soumission */
    @Column(nullable = false)
    private LocalDateTime dateCreation;

    /** Date de traitement par l'admin */
    @Column
    private LocalDateTime dateTraitement;

    /** Commentaire de l'administrateur */
    @Column(length = 500)
    private String commentaireAdmin;

    /** Mot de passe initial (avant hachage) — stocké temporairement */
    @Column(length = 200)
    private String motDePasseInitial;

    /** Statuts possibles d'une demande */
    public enum StatutDemande {
        EN_ATTENTE, APPROUVEE, REJETEE
    }
}