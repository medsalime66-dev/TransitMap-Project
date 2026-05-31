package com.transitmap.repository;

import com.transitmap.entity.LigneInterurbaine;
import com.transitmap.entity.VilleEtape;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VilleEtapeRepository
        extends JpaRepository<VilleEtape, Long> {

    List<VilleEtape> findByLigneOrderByOrdreAsc(LigneInterurbaine ligne);
}