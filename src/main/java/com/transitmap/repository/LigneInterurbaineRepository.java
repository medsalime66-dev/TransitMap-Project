package com.transitmap.repository;

import com.transitmap.entity.LigneInterurbaine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LigneInterurbaineRepository
        extends JpaRepository<LigneInterurbaine, Long> {

    List<LigneInterurbaine> findByVilleDepartAndVilleArrivee(
            String villeDepart, String villeArrivee);

    List<LigneInterurbaine> findByVilleDepartOrVilleArrivee(
            String villeDepart, String villeArrivee);

    List<LigneInterurbaine> findByCreateurUsername(String username);
}