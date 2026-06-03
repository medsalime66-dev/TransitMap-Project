package com.transitmap.controller.agent;

import com.transitmap.dto.ChauffeurDto;
import com.transitmap.repository.LigneRepository;
import com.transitmap.service.agent.AgentChauffeurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Contrôleur agent pour la gestion des chauffeurs.
 */
@Controller
@RequestMapping("/agent/chauffeurs")
@RequiredArgsConstructor
public class AgentChauffeurController {

    private final AgentChauffeurService chauffeurService;
    private final LigneRepository ligneRepository;

    /** Liste tous les chauffeurs de l'agent */
    @GetMapping
    public String lister(Model model, Principal principal) {
        model.addAttribute("chauffeurs",
                chauffeurService.trouverParAgent(principal.getName()));
        return "agent/chauffeurs/liste";
    }

    /** Formulaire de création d'un chauffeur */
    @GetMapping("/creer")
    public String formulaireCreation(Model model) {
        model.addAttribute("chauffeur", new ChauffeurDto());
        model.addAttribute("lignes", ligneRepository.findAll());
        return "agent/chauffeurs/creer";
    }

    /** Traite la création d'un chauffeur */
    @PostMapping("/creer")
    public String creer(
            @Valid @ModelAttribute("chauffeur") ChauffeurDto dto,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("lignes", ligneRepository.findAll());
            return "agent/chauffeurs/creer";
        }

        chauffeurService.creerChauffeur(dto, principal.getName());
        return "redirect:/agent/chauffeurs";
    }

    /** Formulaire de modification d'un chauffeur */
    @GetMapping("/modifier/{id}")
    public String formulaireModification(
            @PathVariable Long id,
            Model model,
            Principal principal) {
        model.addAttribute("chauffeur",
                chauffeurService.trouverParId(id, principal.getName()));
        model.addAttribute("lignes", ligneRepository.findAll());
        return "agent/chauffeurs/modifier";
    }

    /** Traite la modification d'un chauffeur */
    @PostMapping("/modifier/{id}")
    public String modifier(
            @PathVariable Long id,
            @Valid @ModelAttribute("chauffeur") ChauffeurDto dto,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("lignes", ligneRepository.findAll());
            return "agent/chauffeurs/modifier";
        }

        chauffeurService.modifierChauffeur(id, dto, principal.getName());
        return "redirect:/agent/chauffeurs";
    }

    /** Assigne une ligne à un chauffeur */
    @PostMapping("/{id}/assigner-ligne")
    public String assignerLigne(
            @PathVariable Long id,
            @RequestParam Long ligneId,
            Principal principal) {
        chauffeurService.assignerLigne(id, ligneId, principal.getName());
        return "redirect:/agent/chauffeurs";
    }

    /** Supprime un chauffeur */
    @GetMapping("/supprimer/{id}")
    public String supprimer(
            @PathVariable Long id,
            Principal principal) {
        chauffeurService.supprimerChauffeur(id, principal.getName());
        return "redirect:/agent/chauffeurs";
    }
}