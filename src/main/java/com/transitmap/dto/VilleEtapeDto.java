package com.transitmap.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VilleEtapeDto {

    private Long id;
    private Long ligneId;

    @NotBlank(message = "Nom ville obligatoire")
    private String nomVille;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Integer ordre;

    private boolean estDepart;
    private boolean estArrivee;
}
