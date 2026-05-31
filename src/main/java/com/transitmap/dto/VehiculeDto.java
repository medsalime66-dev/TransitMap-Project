package com.transitmap.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculeDto{
    private Long id;
    @NotBlank(message="Matricule obligatoire")
    private String matricule;
    @NotNull(message="Capacite obligatoire")
    @Min(1)
    private Integer capacite;
    @NotBlank(message="Statut obligatoire")
    private String statut;
    private Double currentLatitude;
    private Double currentLongitude;
    @NotNull(message="Ligne obligatoire")
    private Long ligneId;
}