package com.transitmap.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO pour la création et la mise à jour d'un chauffeur.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChauffeurDto {

    private Long id;

    @NotBlank(message = "Le nom complet est obligatoire")
    @Size(max = 150)
    private String nomComplet;

    @NotBlank(message = "Le numéro de permis est obligatoire")
    @Size(max = 50)
    private String numeroPermis;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Size(max = 20)
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    /** Ligne assignée (optionnel) */
    private Long ligneId;

    /** Nom de la ligne (lecture seule) */
    private String ligneNom;

    /** Matricule du véhicule assigné (lecture seule) */
    private String vehiculeMatricule;
}