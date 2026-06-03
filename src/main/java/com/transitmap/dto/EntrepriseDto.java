package com.transitmap.dto;

import com.transitmap.entity.Entreprise.TypeService;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO pour la gestion des entreprises de transport.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EntrepriseDto {

    private Long id;

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Size(max = 200)
    private String nom;

    @Size(max = 100)
    private String numeroCommercial;

    @Size(max = 300)
    private String adresse;

    @NotNull(message = "Le type de service est obligatoire")
    private TypeService typeService;

    /** Codes de paiement par portefeuille */
    private String codeBankily;
    private String codeMasrvi;
    private String codeSedad;
    private String codeClick;
    private String codeBamis;
    private String codeBimbank;
    private String codeBciPay;

    /** Nom d'utilisateur de l'agent propriétaire */
    private String agentUsername;
}