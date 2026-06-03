package com.transitmap.dto;

import com.transitmap.entity.Reservation.StatutReservation;
import com.transitmap.entity.Reservation.TypePaiement;
import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO pour la création et la consultation des réservations.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReservationDto {

    private Long id;

    /** ID du trajet à réserver */
    private Long trajetId;

    /** ID de l'arrêt de départ */
    private Long arretDepartId;

    /** ID de l'arrêt d'arrivée */
    private Long arretArriveeId;

    /** Nom de l'arrêt de départ (lecture seule) */
    private String arretDepartNom;

    /** Nom de l'arrêt d'arrivée (lecture seule) */
    private String arretArriveeNom;

    /** Nom de la ligne (lecture seule) */
    private String ligneNom;

    /** Date du trajet (lecture seule) */
    private String dateTrajet;

    /** Heure de départ (lecture seule) */
    private String heureDepart;

    /** QR code généré (lecture seule) */
    private String codeQR;

    /** Code textuel court (lecture seule) */
    private String codeTexte;

    /** Type de paiement */
    private TypePaiement typePaiement;

    /** Statut de la réservation */
    private StatutReservation statut;

    /** Montant payé */
    private Double montant;

    /** Date de réservation */
    private LocalDateTime dateReservation;
}