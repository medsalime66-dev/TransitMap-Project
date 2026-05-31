package com.transitmap.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "trajets")

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false
    )
    private LocalDate dateTrajet;

    @Column(
            nullable = false
    )
    private LocalTime heureDepart;

    @Column(
            nullable = false
    )
    private LocalTime heureArrivee;

    @Column(
            nullable = false,
            length = 50
    )
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(
            name = "ligne_id",
            nullable = false
    )
    private Ligne ligne;
}