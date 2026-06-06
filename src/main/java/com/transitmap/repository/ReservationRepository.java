package com.transitmap.repository;

import com.transitmap.entity.Reservation;
import com.transitmap.entity.Reservation.StatutReservation;
import com.transitmap.entity.Trajet;
import com.transitmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des réservations des voyageurs.
 */
public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    /** Trouve toutes les réservations d'un voyageur */
    List<Reservation> findByVoyageurOrderByDateReservationDesc(User voyageur);

    /** Trouve les réservations d'un trajet */
    List<Reservation> findByTrajet(Trajet trajet);

    /** Trouve les réservations d'un trajet par statut */
    List<Reservation> findByTrajetAndStatut(
            Trajet trajet, StatutReservation statut);

    /** Trouve une réservation par son code textuel */
    Optional<Reservation> findByCodeTexte(String codeTexte);

    /** Trouve une réservation par son QR code */
    Optional<Reservation> findByCodeQR(String codeQR);

    /** Compte les réservations confirmées pour un trajet */
    long countByTrajetAndStatutIn(
            Trajet trajet, List<StatutReservation> statuts);
}