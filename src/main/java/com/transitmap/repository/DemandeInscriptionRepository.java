package com.transitmap.repository;

import com.transitmap.entity.DemandeInscription;
import com.transitmap.entity.DemandeInscription.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des demandes d'inscription des agents.
 */
public interface DemandeInscriptionRepository
        extends JpaRepository<DemandeInscription, Long> {

    /** Trouve toutes les demandes par statut */
    List<DemandeInscription> findByStatutOrderByDateCreationDesc(
            StatutDemande statut);

    /** Trouve toutes les demandes triées par date */
    List<DemandeInscription> findAllByOrderByDateCreationDesc();

    /** Trouve une demande par email */
    Optional<DemandeInscription> findByEmail(String email);

    /** Vérifie l'existence d'un email */
    boolean existsByEmail(String email);
}