package com.transitmap.controller.voyageur;

import com.transitmap.entity.Ligne;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.LigneRepository;
import com.transitmap.repository.TrajetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour les vues voyageur — carte et trajets par ligne.
 */
@Controller
@RequestMapping("/voyageur")
@RequiredArgsConstructor
public class VoyageurController {

    private final LigneRepository ligneRepository;
    private final TrajetRepository trajetRepository;

    /** Affiche les trajets disponibles pour une ligne */
    @GetMapping("/lignes/{id}/trajets")
    public String trajetsDeLigne(
            @PathVariable Long id, Model model) {
        Ligne ligne = ligneRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ligne introuvable"));
        model.addAttribute("ligne", ligne);
        model.addAttribute("trajets",
                trajetRepository.findByLigne(ligne));
        return "voyageur/ligne-trajets";
    }
}