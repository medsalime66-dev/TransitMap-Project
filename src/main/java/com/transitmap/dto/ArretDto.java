package com.transitmap.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArretDto{
    private Long id;
    @NotBlank(message="Nom obligatoire")
    private String nom;
    @NotNull(message="Latitude obligatoire")
    private Double latitude;
    @NotNull(message="Longitude obligatoire")
    private Double longitude;
    @NotNull(message="Ordre obligatoire")
    private Integer ordre;
    @NotNull(message="Ligne obligatoire")
    private Long ligneId;
}