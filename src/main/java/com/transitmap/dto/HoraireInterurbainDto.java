package com.transitmap.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HoraireInterurbainDto {

    private Long id;
    private Long ligneId;

    @NotNull(message = "Heure départ obligatoire")
    private LocalTime heureDepart;

    @NotNull(message = "Heure arrivée obligatoire")
    private LocalTime heureArrivee;

    @NotBlank(message = "Jours obligatoire")
    private String jours;

    @NotNull(message = "Prix obligatoire")
    @Min(0)
    private Double prix;

    private Boolean actif;
}