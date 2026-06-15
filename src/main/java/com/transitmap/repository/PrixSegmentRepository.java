package com.transitmap.repository;

import com.transitmap.entity.PrixSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrixSegmentRepository extends JpaRepository<PrixSegment, Long> {

    List<PrixSegment> findByLigneId(Long ligneId);

    @Query("""
        SELECT p FROM PrixSegment p
        WHERE p.ligne.id = :ligneId
          AND ((p.arretA.id = :a AND p.arretB.id = :b)
            OR (p.arretA.id = :b AND p.arretB.id = :a))
        """)
    Optional<PrixSegment> findPrixEntre(
            @Param("ligneId") Long ligneId,
            @Param("a") Long a,
            @Param("b") Long b);
}
