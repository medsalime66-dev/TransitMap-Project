package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lignes_interurbaines")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LigneInterurbaine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String villeDepart;

    @Column(nullable = false, length = 100)
    private String villeArrivee;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createur_id")
    private User createur;

    @OneToMany(mappedBy = "ligne", cascade = CascadeType.ALL,
               fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("ordre ASC")
    @Builder.Default
    private List<VilleEtape> etapes = new ArrayList<>();
}
