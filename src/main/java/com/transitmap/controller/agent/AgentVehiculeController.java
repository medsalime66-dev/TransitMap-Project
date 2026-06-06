package com.transitmap.controller.agent;

import com.transitmap.dto.VehiculeDto;
import com.transitmap.repository.LigneRepository;
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
 * Contrôleur agent pour la gestion des véhicules.
 */
@Controller
@RequestMapping("/agent/vehicules")
@RequiredArgsConstructor
public class AgentVehiculeController {

    private final AgentVehiculeService vehiculeService;
    private final AgentChauffeurService chauffeurService;
    private final LigneRepository ligneRepository;

    /** Liste tous les véhicules de l'agent */
    @GetMapping
    public String lister(Model model, Principal principal) {
        model.addAttribute("vehicules",
                vehiculeService.trouverParAgent(principal.getName()));
        return "agent/vehicules/liste";
    }

    /** Formulaire de création d'un véhicule */
    @GetMapping("/creer")
    public String formulaireCreation(Model model, Principal principal) {
        model.addAttribute("vehicule", new VehiculeDto());
        model.addAttribute("lignes", ligneRepository.findAll());
        model.addAttribute("chauffeurs",
                chauffeurService.trouverParAgent(principal.getName()));
        return "agent/vehicules/creer";
    }

    /** Traite la création d'un véhicule */
    @PostMapping("/creer")
    public String creer(
            @Valid @ModelAttribute("vehicule") VehiculeDto dto,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("lignes", ligneRepository.findAll());
            model.addAttribute("chauffeurs",
                    chauffeurService.trouverParAgent(principal.getName()));
            return "agent/vehicules/creer";
        }

        vehiculeService.creerVehicule(dto, principal.getName());
        return "redirect:/agent/vehicules";
    }

    /** Formulaire de modification d'un véhicule */
    @GetMapping("/modifier/{id}")
    public String formulaireModification(
            @PathVariable Long id,
            Model model,
            Principal principal) {
        model.addAttribute("vehicule",
                vehiculeService.trouverParId(id));
        model.addAttribute("lignes", ligneRepository.findAll());
        model.addAttribute("chauffeurs",
                chauffeurService.trouverParAgent(principal.getName()));
        return "agent/vehicules/modifier";
    }

    /** Traite la modification d'un véhicule */
    @PostMapping("/modifier/{id}")
    public String modifier(
            @PathVariable Long id,
            @Valid @ModelAttribute("vehicule") VehiculeDto dto,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("lignes", ligneRepository.findAll());
            model.addAttribute("chauffeurs",
                    chauffeurService.trouverParAgent(principal.getName()));
            return "agent/vehicules/modifier";
        }

        vehiculeService.modifierVehicule(id, dto, principal.getName());
        return "redirect:/agent/vehicules";
    }

    /** Assigne un chauffeur à un véhicule */
    @PostMapping("/{id}/assigner-chauffeur")
    public String assignerChauffeur(
            @PathVariable Long id,
            @RequestParam Long chauffeurId,
            Principal principal) {
        vehiculeService.assignerChauffeur(id, chauffeurId,
                principal.getName());
        return "redirect:/agent/vehicules";
    }

    /** Supprime un véhicule */
    @GetMapping("/supprimer/{id}")
    public String supprimer(
            @PathVariable Long id,
            Principal principal) {
        vehiculeService.supprimerVehicule(id, principal.getName());
        return "redirect:/agent/vehicules";
    }
}