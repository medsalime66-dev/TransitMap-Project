package com.transitmap.repository;

import com.transitmap.entity.HoraireInterurbain;
import com.transitmap.entity.LigneInterurbaine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HoraireInterurbaineRepository
        extends JpaRepository<HoraireInterurbain, Long> {

    List<HoraireInterurbain> findByLigneAndActifTrue(LigneInterurbaine ligne);

    List<HoraireInterurbain> findByLigne(LigneInterurbaine ligne);
}