package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prix_segments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrixSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_id", nullable = false)
    private LigneInterurbaine ligne;

    /** Première station du couple (symétrique) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arret_a_id", nullable = false)
    private VilleEtape arretA;

    /** Deuxième station du couple (symétrique) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arret_b_id", nullable = false)
    private VilleEtape arretB;

    @Column(nullable = false)
    private Double prix;
}
