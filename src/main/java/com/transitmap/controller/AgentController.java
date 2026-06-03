package com.transitmap.controller;

import com.transitmap.dto.ArretDto;
import com.transitmap.dto.TrajetDto;
import com.transitmap.repository.LigneRepository;
import com.transitmap.service.ArretService;
import com.transitmap.service.TrajetService;
import com.transitmap.service.agent.AgentChauffeurService;
import com.transitmap.service.agent.AgentVehiculeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Contrôleur principal de l'agent.
 * Gère le tableau de bord et les opérations de base.
 */
@Controller
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentController {

    private final ArretService arretService;
    private final TrajetService trajetService;
    private final LigneRepository ligneRepository;
    private final AgentChauffeurService chauffeurService;
    private final AgentVehiculeService vehiculeService;

    /** Tableau de bord de l'agent */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();
        var chauffeurs = chauffeurService.trouverParAgent(username);
        var vehicules = vehiculeService.trouverParAgent(username);

        model.addAttribute("chauffeurs", chauffeurs);
        model.addAttribute("vehicules", vehicules);
        model.addAttribute("totalChauffeurs", chauffeurs.size());
        model.addAttribute("totalVehicules", vehicules.size());
        model.addAttribute("lignes", ligneRepository.findAll());
        return "agent/dashboard";
    }

    /** Formulaire d'ajout d'un arrêt */
    @GetMapping("/arrets/create")
    public String createArretForm(Model model) {
        model.addAttribute("arret", new ArretDto());
        model.addAttribute("lignes", ligneRepository.findAll());
        return "agent/arret-create";
    }

    /** Traite l'ajout d'un arrêt */
    @PostMapping("/arrets/create")
    public String createArret(
            @Valid @ModelAttribute("arret") ArretDto dto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("lignes", ligneRepository.findAll());
            return "agent/arret-create";
        }
        arretService.create(dto);
        return "redirect:/agent/dashboard";
    }

    /** Formulaire d'ajout d'un trajet */
    @GetMapping("/trajets/create")
    public String createTrajetForm(Model model) {
        model.addAttribute("trajet", new TrajetDto());
        model.addAttribute("lignes", ligneRepository.findAll());
        return "agent/trajet-create";
    }

    /** Traite l'ajout d'un trajet */
    @PostMapping("/trajets/create")
    public String createTrajet(
            @Valid @ModelAttribute("trajet") TrajetDto dto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("lignes", ligneRepository.findAll());
            return "agent/trajet-create";
        }
        trajetService.create(dto);
        return "redirect:/agent/dashboard";
    }
}