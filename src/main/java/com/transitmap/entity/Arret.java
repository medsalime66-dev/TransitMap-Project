package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "arrets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Arret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 150
    )
    private String nom;

    @Column(
            nullable = false
    )
    private Double latitude;

    @Column(
            nullable = false
    )
    private Double longitude;

    @Column(
            nullable = false
    )
    private Integer ordre;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(
            name = "ligne_id",
            nullable = false
    )
    private Ligne ligne;
}