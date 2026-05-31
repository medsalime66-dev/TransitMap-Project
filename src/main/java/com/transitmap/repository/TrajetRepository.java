package com.transitmap.repository;

import com.transitmap.entity.Trajet;
import com.transitmap.entity.Ligne;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrajetRepository
        extends JpaRepository<Trajet, Long> {

    List<Trajet> findByDateTrajet(
            LocalDate date
    );

    List<Trajet> findByLigne(
            Ligne ligne
    );

    List<Trajet> findByDateTrajetAndLigne(
            LocalDate date,
            Ligne ligne
    );

    List<Trajet> findByStatut(
            String statut
    );
}