package com.transitmap.controller.agent;

import com.transitmap.repository.*;
import com.transitmap.service.agent.AgentChauffeurService;
import com.transitmap.service.agent.AgentVehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Contrôleur du tableau de bord de l'agent.
 * Affiche un résumé de toutes les ressources de l'agent.
 */
@Controller
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentDashboardController {

    private final LigneRepository ligneRepository;
    private final AgentVehiculeService vehiculeService;
    private final AgentChauffeurService chauffeurService;
    private final TrajetRepository trajetRepository;

    /** Tableau de bord principal de l'agent */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();

        model.addAttribute("totalLignes",
                ligneRepository.count());
        model.addAttribute("vehicules",
                vehiculeService.trouverParAgent(username));
        model.addAttribute("chauffeurs",
                chauffeurService.trouverParAgent(username));
        model.addAttribute("totalChauffeurs",
                chauffeurService.trouverParAgent(username).size());
        model.addAttribute("totalVehicules",
                vehiculeService.trouverParAgent(username).size());

        return "agent/dashboard";
    }
}