package com.transitmap.repository;

import com.transitmap.entity.Arret;
import com.transitmap.entity.Ligne;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArretRepository
        extends JpaRepository<Arret, Long> {

    List<Arret> findByLigneOrderByOrdreAsc(
            Ligne ligne
    );

    List<Arret> findByNomContainingIgnoreCase(
            String keyword
    );
    List<Arret> findByLigneIdOrderByOrdreAsc(
        Long ligneId
);
}