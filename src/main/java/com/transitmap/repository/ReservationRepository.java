package com.transitmap.repository;

import com.transitmap.entity.Reservation;
import com.transitmap.entity.Reservation.StatutReservation;
import com.transitmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByVoyageurOrderByDateReservationDesc(User voyageur);

    Optional<Reservation> findByCodeTexte(String codeTexte);

    Optional<Reservation> findByCodeQR(String codeQR);

    List<Reservation> findByStatut(StatutReservation statut);
}
