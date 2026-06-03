package com.transitmap.repository;

import com.transitmap.entity.Chauffeur;
import com.transitmap.entity.Ligne;
import com.transitmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des chauffeurs.
 */
public interface ChauffeurRepository
        extends JpaRepository<Chauffeur, Long> {

    /** Trouve tous les chauffeurs créés par un agent */
    List<Chauffeur> findByAgent(User agent);

    /** Trouve un chauffeur par son email */
    Optional<Chauffeur> findByEmail(String email);

    /** Trouve un chauffeur par son numéro de permis */
    Optional<Chauffeur> findByNumeroPermis(String numeroPermis);

    /** Trouve les chauffeurs assignés à une ligne */
    List<Chauffeur> findByLigne(Ligne ligne);

    /** Vérifie l'existence d'un email */
    boolean existsByEmail(String email);

    /** Vérifie l'existence d'un numéro de permis */
    boolean existsByNumeroPermis(String numeroPermis);
}