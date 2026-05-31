package com.transitmap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AgentRequestDto {

    private Long id;

    @NotBlank(message = "Nom de la ligne obligatoire")
    private String nomLigne;

    @NotBlank(message = "Numéro de la ligne obligatoire")
    private String numeroLigne;

    private String description;

    private String statut;

    private String agentUsername;

    private LocalDateTime dateCreation;

    private LocalDateTime dateTraitement;

    private String commentaireAdmin;
}