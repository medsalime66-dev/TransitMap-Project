package com.transitmap.controller;

import com.transitmap.repository.ArretRepository;
import com.transitmap.repository.LigneRepository;
import com.transitmap.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * API REST publique pour les données de la carte.
 * Fournit les arrêts, véhicules et lignes en JSON.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MapApiController {

    private final ArretRepository arretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final LigneRepository ligneRepository;

    /** Retourne tous les arrêts avec leurs coordonnées */
    @GetMapping("/arrets")
    public Object arrets() {
        return arretRepository.findAll();
    }

    /** Retourne tous les véhicules avec leur position GPS */
    @GetMapping("/vehicules")
    public Object vehicules() {
        return vehiculeRepository.findAll();
    }

    /** Retourne toutes les lignes */
    @GetMapping("/lignes")
    public Object lignes() {
        return ligneRepository.findAll();
    }
}