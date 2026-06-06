package com.transitmap.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO pour la création et la mise à jour des véhicules.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VehiculeDto {

    private Long id;

    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;

    /** Marque et modèle */
    private String marque;

    @NotNull(message = "La capacité est obligatoire")
    @Min(1)
    private Integer capacite;

    /** Places actuellement disponibles */
    private Integer placesDisponibles;

    /** Année de fabrication */
    private Integer annee;

    @NotBlank(message = "Le statut est obligatoire")
    private String statut;

    /** Position GPS — latitude */
    private Double latitudeActuelle;

    /** Position GPS — longitude */
    private Double longitudeActuelle;

    @NotNull(message = "La ligne est obligatoire")
    private Long ligneId;

    /** Chauffeur assigné (optionnel) */
    private Long chauffeurId;
}