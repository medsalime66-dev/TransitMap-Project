package com.transitmap.controller;

import com.transitmap.dto.*;
import com.transitmap.service.InterurbainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/interurbain")
@RequiredArgsConstructor
public class InterurbainController {

    private final InterurbainService interurbainService;

    // قائمة الخطوط
    @GetMapping
    public String list(Model model) {
        model.addAttribute("lignes", interurbainService.findAllLignes());
        return "interurbain/list";
    }

    // تفاصيل خط
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("ligne", interurbainService.findLigneById(id));
        model.addAttribute("horaire", new HoraireInterurbainDto());
        model.addAttribute("etape", new VilleEtapeDto());
        return "interurbain/detail";
    }

    // بحث
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