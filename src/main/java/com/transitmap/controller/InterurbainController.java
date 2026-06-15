package com.transitmap.controller;

import com.transitmap.service.InterurbainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/interurbain")
@RequiredArgsConstructor
public class InterurbainController {

    private final InterurbainService interurbainService;

    // /interurbain  ->  redirige vers la page de recherche
    @GetMapping
    public String index() {
        return "redirect:/interurbain/search";
    }

    // Recherche de voyages par villes (départ / arrivée)
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String villeDepart,
            @RequestParam(required = false) String villeArrivee,
            Model model) {

        model.addAttribute("villes", List.of(
            "Nouakchott","Nouadhibou","Rosso","Kaédi","Zouerate",
            "Atar","Tidjikja","Kiffa","Sélibaby","Néma",
            "Aleg","Boutilimit","Akjoujt","Ouadane","Chinguetti"
        ));

        if (villeDepart != null && villeArrivee != null
                && !villeDepart.isEmpty() && !villeArrivee.isEmpty()) {
            model.addAttribute("resultats",
                    interurbainService.search(villeDepart, villeArrivee));
            model.addAttribute("villeDepart", villeDepart);
            model.addAttribute("villeArrivee", villeArrivee);
        }
        return "interurbain/search";
    }
}