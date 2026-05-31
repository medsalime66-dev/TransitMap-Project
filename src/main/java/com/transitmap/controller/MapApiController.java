package com.transitmap.controller;

import com.transitmap.repository.ArretRepository;
import com.transitmap.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MapApiController{

    private final ArretRepository arretRepository;
    private final VehiculeRepository vehiculeRepository;

    @GetMapping("/arrets")
    public Object arrets(){
        return arretRepository.findAll();
    }

    @GetMapping("/vehicules")
    public Object vehicules(){
        return vehiculeRepository.findAll();
    }
}