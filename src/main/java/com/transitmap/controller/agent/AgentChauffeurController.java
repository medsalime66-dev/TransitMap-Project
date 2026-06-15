package com.transitmap.controller.agent;

import com.transitmap.dto.ChauffeurDto;
import com.transitmap.service.agent.AgentChauffeurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/agent/chauffeurs")
@RequiredArgsConstructor
public class AgentChauffeurController {

    private final AgentChauffeurService chauffeurService;

    @GetMapping
    public String lister(Model model, Principal principal) {
        model.addAttribute("chauffeurs", chauffeurService.trouverParAgent(principal.getName()));
        return "agent/chauffeurs/liste";
    }

    @GetMapping("/creer")
    public String formulaireCreation(Model model) {
        model.addAttribute("chauffeur", new ChauffeurDto());
        return "agent/chauffeurs/creer";
    }

    @PostMapping("/creer")
    public String creer(
            @Valid @ModelAttribute("chauffeur") ChauffeurDto dto,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) return "agent/chauffeurs/creer";

        try {
            chauffeurService.creerChauffeur(dto, principal.getName());
        } catch (RuntimeException e) {
            model.addAttribute("erreur", e.getMessage());
            return "agent/chauffeurs/creer";
        }
        return "redirect:/agent/chauffeurs";
    }

    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("chauffeur", chauffeurService.trouverParId(id, principal.getName()));
        return "agent/chauffeurs/modifier";
    }

    @PostMapping("/modifier/{id}")
    public String modifier(
            @PathVariable Long id,
            @Valid @ModelAttribute("chauffeur") ChauffeurDto dto,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) return "agent/chauffeurs/modifier";
        chauffeurService.modifierChauffeur(id, dto, principal.getName());
        return "redirect:/agent/chauffeurs";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, Principal principal) {
        chauffeurService.supprimerChauffeur(id, principal.getName());
        return "redirect:/agent/chauffeurs";
    }
}
