package com.transitmap.controller;

import com.transitmap.entity.LigneInterurbaine;
import com.transitmap.entity.VilleEtape;
import com.transitmap.repository.LigneInterurbaineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/map-api")
@RequiredArgsConstructor
public class MapApiController {

    private final LigneInterurbaineRepository ligneRepo;

    @GetMapping("/lignes")
    public List<Map<String, Object>> lignes() {
        List<Map<String, Object>> result = new ArrayList<>();

        for (LigneInterurbaine ligne : ligneRepo.findAll()) {
            Map<String, Object> ligneMap = new LinkedHashMap<>();
            ligneMap.put("id", ligne.getId());
            ligneMap.put("nom", ligne.getNom());
            ligneMap.put("villeDepart", ligne.getVilleDepart());
            ligneMap.put("villeArrivee", ligne.getVilleArrivee());

            List<Map<String, Object>> etapesList = new ArrayList<>();
            List<VilleEtape> etapes = new ArrayList<>(ligne.getEtapes());
            etapes.sort(Comparator.comparingInt(VilleEtape::getOrdre));

            for (VilleEtape e : etapes) {
                Map<String, Object> etapeMap = new LinkedHashMap<>();
                etapeMap.put("id", e.getId());
                etapeMap.put("nomVille", e.getNomVille());
                etapeMap.put("latitude", e.getLatitude());
                etapeMap.put("longitude", e.getLongitude());
                etapeMap.put("ordre", e.getOrdre());
                etapeMap.put("estDepart", e.isEstDepart());
                etapeMap.put("estArrivee", e.isEstArrivee());
                etapesList.add(etapeMap);
            }
            ligneMap.put("etapes", etapesList);
            result.add(ligneMap);
        }
        return result;
    }
}