package com.transitmap.dto;

import com.transitmap.entity.Paiement.MethodePaiement;
import com.transitmap.entity.Paiement.StatutPaiement;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO pour la soumission et la consultation des paiements.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PaiementDto {

    private Long id;

    /** ID de la réservation associée */
    private Long reservationId;

    /** Méthode de paiement choisie */
    private MethodePaiement methode;

    /** Montant en MRU */
    private Double montant;

    /** Code de transaction saisi par le voyageur */
    private String codeTransaction;

    /** Code commerçant de l'agent (affiché à l'écran) */
    private String codeCommerçant;

    /** Statut du paiement */
    private StatutPaiement statut;

    /** Date de validation */
    private LocalDateTime dateValidation;
}