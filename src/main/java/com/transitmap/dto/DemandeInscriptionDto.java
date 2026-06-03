package com.transitmap.dto;

import com.transitmap.entity.DemandeInscription.StatutDemande;
import com.transitmap.entity.Entreprise.TypeService;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO pour la soumission et la consultation des demandes d'inscription.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DemandeInscriptionDto {

    private Long id;

    // === Informations personnelles ===

    @NotBlank(message = "Le nom complet est obligatoire")
    @Size(max = 150)
    private String nomComplet;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Size(max = 20)
    private String telephone;

    @Size(max = 50)
    private String numeroCIN;

    // === Informations entreprise ===

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Size(max = 200)
    private String nomEntreprise;

    @Size(max = 100)
    private String numeroCommercial;

    @Size(max = 300)
    private String adresse;

    @NotNull(message = "Le type de service est obligatoire")
    private TypeService typeService;

    @Size(max = 1000)
    private String description;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6)
    private String motDePasse;

    // === Codes de paiement (au moins un obligatoire) ===

    private String codeBankily;
    private String codeMasrvi;
    private String codeSedad;
    private String codeClick;
    private String codeBamis;
    private String codeBimbank;
    private String codeBciPay;

    // === Lecture seule ===

    private StatutDemande statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateTraitement;
    private String commentaireAdmin;
}