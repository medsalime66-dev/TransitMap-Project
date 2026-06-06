package com.transitmap.dto;

import com.transitmap.entity.DemandeInscription.StatutDemande;
import com.transitmap.entity.Entreprise.TypeService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour les demandes d'inscription agent.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandeInscriptionDto {

    private Long id;

    @NotBlank(message = "Le nom complet est obligatoire")
    private String nomComplet;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    private String numeroCIN;

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String nomEntreprise;

    @NotBlank(message = "Le registre commercial est obligatoire")
    private String numeroCommercial;

    private String adresse;

    @NotBlank(message = "Le type de service est obligatoire")
    private String typeService;

    private String description;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Minimum 6 caractères")
    private String motDePasseInitial;

    @NotBlank(message = "La confirmation est obligatoire")
    private String confirmerMotDePasse;

    private String codeBankily;
    private String codeMasrvi;
    private String codeSedad;
    private String codeClick;
    private String codeBamis;
    private String codeBimbank;
    private String codeBciPay;

    private StatutDemande statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateTraitement;
    private String commentaireAdmin;
}