package com.transitmap.controller.agent;

import com.transitmap.dto.VehiculeDto;
import com.transitmap.service.agent.AgentChauffeurService;
import com.transitmap.service.agent.AgentVehiculeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/agent/vehicules")
@RequiredArgsConstructor
public class AgentVehiculeController {

    private final AgentVehiculeService vehiculeService;
    private final AgentChauffeurService chauffeurService;

    @GetMapping
    public String lister(Model model, Principal principal) {
        model.addAttribute("vehicules", vehiculeService.trouverParAgent(principal.getName()));
        return "agent/vehicules/liste";
    }

    @GetMapping("/creer")
    public String formulaireCreation(Model model, Principal principal) {
        model.addAttribute("vehicule", new VehiculeDto());
        model.addAttribute("chauffeurs", chauffeurService.trouverParAgent(principal.getName()));
        return "agent/vehicules/creer";
    }

    @PostMapping("/creer")
    public String creer(
            @Valid @ModelAttribute("vehicule") VehiculeDto dto,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("chauffeurs", chauffeurService.trouverParAgent(principal.getName()));
            return "agent/vehicules/creer";
        }
        vehiculeService.creerVehicule(dto, principal.getName());
        return "redirect:/agent/vehicules";
    }

    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute("vehicule", vehiculeService.trouverParId(id));
        model.addAttribute("chauffeurs", chauffeurService.trouverParAgent(principal.getName()));
        return "agent/vehicules/modifier";
    }

    @PostMapping("/modifier/{id}")
    public String modifier(
            @PathVariable Long id,
            @Valid @ModelAttribute("vehicule") VehiculeDto dto,
            BindingResult result,
            Model model,
            Principal principal) {

        if (result.hasErrors()) {
            model.addAttribute("chauffeurs", chauffeurService.trouverParAgent(principal.getName()));
            return "agent/vehicules/modifier";
        }
        vehiculeService.modifierVehicule(id, dto, principal.getName());
        return "redirect:/agent/vehicules";
    }

    @PostMapping("/{id}/assigner-chauffeur")
    public String assignerChauffeur(@PathVariable Long id, @RequestParam Long chauffeurId, Principal principal) {
        vehiculeService.assignerChauffeur(id, chauffeurId, principal.getName());
        return "redirect:/agent/vehicules";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, Principal principal) {
        vehiculeService.supprimerVehicule(id, principal.getName());
        return "redirect:/agent/vehicules";
    }
}
