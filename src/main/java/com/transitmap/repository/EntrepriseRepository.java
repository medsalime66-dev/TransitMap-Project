package com.transitmap.repository;

import com.transitmap.entity.Entreprise;
import com.transitmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /** Entreprise qui exploite la ligne (via le créateur de la ligne). */
    @Query("""
        SELECT e FROM Entreprise e
        WHERE e.agent.id = (
            SELECT l.createur.id FROM LigneInterurbaine l WHERE l.id = :ligneId
        )
        """)
    Optional<Entreprise> findByLigneId(@Param("ligneId") Long ligneId);
}