package com.transitmap.service.voyageur;

import com.transitmap.dto.ReservationDto;
import java.util.List;

public interface ReservationService {

    ReservationDto creerReservation(ReservationDto dto, String usernameVoyageur);

    List<ReservationDto> mesReservations(String usernameVoyageur);

    ReservationDto trouverParId(Long id, String usernameVoyageur);

    ReservationDto confirmerApresPaiement(Long reservationId);

    void annuler(Long id, String usernameVoyageur);

    ReservationDto validerCode(String codeTexte);

    /** Calcule le prix d'un segment entre deux arrêts (matrice PrixSegment). */
    Double calculerPrix(Long ligneId, Long arretDepartId, Long arretArriveeId);
}