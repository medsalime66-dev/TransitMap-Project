package com.transitmap.controller;

import com.transitmap.repository.ArretRepository;
import com.transitmap.repository.LigneRepository;
import com.transitmap.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MapApiController {

    private final ArretRepository arretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final LigneRepository ligneRepository;

    @GetMapping("/arrets")
    public Object arrets() {
        return arretRepository.findAll();
    }

    @GetMapping("/vehicules")
    public Object vehicules() {
        return vehiculeRepository.findAll();
    }

    @GetMapping("/lignes")
    public Object lignes() {
        return ligneRepository.findAll();
    }
}