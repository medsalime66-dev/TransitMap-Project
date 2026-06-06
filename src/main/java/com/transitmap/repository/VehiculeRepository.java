package com.transitmap.repository;

import com.transitmap.entity.Vehicule;
import com.transitmap.entity.Ligne;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculeRepository
        extends JpaRepository<Vehicule, Long> {

    Optional<Vehicule> findByMatricule(
            String matricule
    );

    List<Vehicule> findByLigne(
            Ligne ligne
    );

    List<Vehicule> findByStatut(
            String statut
    );
}