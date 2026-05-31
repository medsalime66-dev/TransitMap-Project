package com.transitmap.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneDto {

    private Long id;

    @NotBlank(message = "Numero is required")
    private String numero;

    @NotBlank(message = "Nom is required")
    private String nom;

    @Size(max = 500)
    private String description;
}