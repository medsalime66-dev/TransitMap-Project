package com.transitmap.repository;

import com.transitmap.entity.Paiement;
import com.transitmap.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository pour la gestion des paiements électroniques.
 */
public interface PaiementRepository
        extends JpaRepository<Paiement, Long> {

    /** Trouve le paiement lié à une réservation */
    Optional<Paiement> findByReservation(Reservation reservation);

    /** Vérifie si une réservation a déjà un paiement */
    boolean existsByReservation(Reservation reservation);
}