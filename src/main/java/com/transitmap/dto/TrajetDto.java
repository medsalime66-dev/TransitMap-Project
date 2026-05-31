package com.transitmap.dto;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetDto {

    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate dateTrajet;

    @NotNull(message = "Heure depart is required")
    private LocalTime heureDepart;

    @NotNull(message = "Heure arrivee is required")
    private LocalTime heureArrivee;

    @NotBlank(message = "Statut is required")
    private String statut;

    @NotNull(message = "Ligne is required")
    private Long ligneId;
}