package com.transitmap.repository;

import com.transitmap.entity.Chauffeur;
import com.transitmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {

    List<Chauffeur> findByAgent(User agent);

    Optional<Chauffeur> findByEmail(String email);

    Optional<Chauffeur> findByNumeroPermis(String numeroPermis);

    boolean existsByEmail(String email);

    boolean existsByNumeroPermis(String numeroPermis);
}
