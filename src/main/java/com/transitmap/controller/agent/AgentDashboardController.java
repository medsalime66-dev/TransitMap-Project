package com.transitmap.controller.agent;

import com.transitmap.repository.LigneRepository;
import com.transitmap.repository.TrajetRepository;
import com.transitmap.service.agent.AgentChauffeurService;
import com.transitmap.service.agent.AgentVehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

/**
 * Contrôleur du tableau de bord de l'agent.
 */
@Controller
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentDashboardController {

    private final LigneRepository ligneRepository;
    private final TrajetRepository trajetRepository;
    private final AgentVehiculeService vehiculeService;
    private final AgentChauffeurService chauffeurService;

    /** Tableau de bord principal de l'agent */
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
        model.addAttribute("trajetsAujourdhui",
                trajetRepository.findByDateTrajet(LocalDate.now()));
        return "agent/dashboard";
    }
}