package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "villes_etapes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VilleEtape {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_id", nullable = false)
    private LigneInterurbaine ligne;

    @Column(nullable = false, length = 100)
    private String nomVille;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer ordre;

    @Column(nullable = false)
    private boolean estDepart;

    @Column(nullable = false)
    private boolean estArrivee;

}