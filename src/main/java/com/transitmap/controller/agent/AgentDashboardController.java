package com.transitmap.controller.agent;

import com.transitmap.service.agent.AgentChauffeurService;
import com.transitmap.service.agent.AgentInterurbainService;
import com.transitmap.service.agent.AgentVehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentDashboardController {

    private final AgentVehiculeService vehiculeService;
    private final AgentChauffeurService chauffeurService;
    private final AgentInterurbainService interurbainService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();
        var chauffeurs = chauffeurService.trouverParAgent(username);
        var vehicules = vehiculeService.trouverParAgent(username);
        var lignes = interurbainService.listerLignes(username);

        model.addAttribute("chauffeurs", chauffeurs);
        model.addAttribute("vehicules", vehicules);
        model.addAttribute("lignes", lignes);
        model.addAttribute("totalChauffeurs", chauffeurs.size());
        model.addAttribute("totalVehicules", vehicules.size());
        model.addAttribute("trajetsAujourdhui", java.util.List.of());
        return "agent/dashboard";
    }
}
