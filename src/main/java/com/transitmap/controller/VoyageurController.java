package com.transitmap.controller;

import com.transitmap.repository.LigneRepository;
import com.transitmap.repository.TrajetRepository;
import com.transitmap.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/voyageur")
@RequiredArgsConstructor
public class VoyageurController {

    private final LigneRepository ligneRepository;
    private final TrajetRepository trajetRepository;

    @GetMapping("/lignes/{id}/trajets")
    public String trajetsByLigne(@PathVariable Long id, Model model) {
        var ligne = ligneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne introuvable"));
        var trajets = trajetRepository.findByLigne(ligne);
        model.addAttribute("ligne", ligne);
        model.addAttribute("trajets", trajets);
        return "voyageur/ligne-trajets";
    }
}