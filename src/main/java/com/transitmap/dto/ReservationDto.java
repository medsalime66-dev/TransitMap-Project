package com.transitmap.dto;

import com.transitmap.entity.Reservation.StatutReservation;
import com.transitmap.entity.Reservation.TypePaiement;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReservationDto {

    private Long id;

    private Long ligneId;
    private Long arretDepartId;
    private Long arretArriveeId;
    private Long horaireId;

    private String arretDepartNom;
    private String arretArriveeNom;
    private String ligneNom;

    private LocalDate dateTrajet;
    private LocalTime heureDepart;

    private String codeQR;
    private String codeTexte;

    private TypePaiement typePaiement;
    private StatutReservation statut;
    private Double montant;
    private LocalDateTime dateReservation;
}