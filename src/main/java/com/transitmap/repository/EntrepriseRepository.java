package com.transitmap.repository;

import com.transitmap.entity.Entreprise;
import com.transitmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository pour la gestion des entreprises de transport.
 */
public interface EntrepriseRepository
        extends JpaRepository<Entreprise, Long> {

    /** Trouve l'entreprise d'un agent */
    Optional<Entreprise> findByAgent(User agent);

    /** Vérifie l'existence d'un numéro commercial */
    boolean existsByNumeroCommercial(String numeroCommercial);
}