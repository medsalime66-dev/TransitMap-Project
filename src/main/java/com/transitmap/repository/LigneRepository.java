package com.transitmap.repository;

import com.transitmap.entity.Ligne;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneRepository
        extends JpaRepository<Ligne, Long> {

    Page<Ligne> findByNomContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    boolean existsByNumero(
            String numero
    );
}