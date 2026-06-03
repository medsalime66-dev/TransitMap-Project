package com.transitmap.service.voyageur;

import com.transitmap.dto.ReservationDto;
import java.util.List;

/**
 * Service de gestion des réservations pour les voyageurs.
 */
public interface ReservationService {

    /** Crée une réservation (statut EN_ATTENTE) */
    ReservationDto creerReservation(ReservationDto dto, String usernameVoyageur);

    /** Retourne toutes les réservations d'un voyageur */
    List<ReservationDto> mesReservations(String usernameVoyageur);

    /** Retourne une réservation par son ID */
    ReservationDto trouverParId(Long id, String usernameVoyageur);

    /** Confirme une réservation après paiement (génère QR + code) */
    ReservationDto confirmerApresPaiement(Long reservationId);

    /** Annule une réservation */
    void annuler(Long id, String usernameVoyageur);

    /** Valide un code textuel (par le chauffeur) */
    ReservationDto validerCode(String codeTexte);

    /** Retourne les réservations en attente pour un trajet (pour chauffeur) */
    List<ReservationDto> reservationsEnAttenteParTrajet(Long trajetId);
}