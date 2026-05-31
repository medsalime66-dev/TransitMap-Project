package com.transitmap.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LigneInterurbaineDto {

    private Long id;

    @NotBlank(message = "Nom obligatoire")
    private String nom;

    @NotBlank(message = "Ville départ obligatoire")
    private String villeDepart;

    @NotBlank(message = "Ville arrivée obligatoire")
    private String villeArrivee;

    @NotNull(message = "Distance obligatoire")
    @Min(1)
    private Double distanceKm;

    @NotNull(message = "Prix de base obligatoire")
    @Min(0)
    private Double prixBase;

    private String description;

    private List<VilleEtapeDto> etapes;
    private List<HoraireInterurbainDto> horaires;
}