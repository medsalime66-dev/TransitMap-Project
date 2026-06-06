package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "horaires_interurbains")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HoraireInterurbain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ligne_id", nullable = false)
    private LigneInterurbaine ligne;

    @Column(nullable = false)
    private LocalTime heureDepart;

    @Column(nullable = false)
    private LocalTime heureArrivee;

    // "LUNDI,MARDI,MERCREDI,JEUDI,VENDREDI,SAMEDI,DIMANCHE" أو "QUOTIDIEN"
    @Column(nullable = false, length = 100)
    private String jours;

    @Column(nullable = false)
    private Double prix;

    @Column(nullable = false)
    private Boolean actif;
}