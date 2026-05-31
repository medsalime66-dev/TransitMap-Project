package com.transitmap.controller;

import com.transitmap.repository.*;
import com.transitmap.service.AgentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LigneRepository ligneRepository;
    private final ArretRepository arretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetRepository trajetRepository;
    private final UserRepository userRepository;
    private final AgentRequestService agentRequestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalLignes", ligneRepository.count());
        model.addAttribute("totalArrets", arretRepository.count());
        model.addAttribute("totalVehicules", vehiculeRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("trajetsAujourdhui",
                trajetRepository.findByDateTrajet(LocalDate.now()));
        model.addAttribute("demandesEnAttente",
                agentRequestService.findAll().stream()
                        .filter(d -> "EN_ATTENTE".equals(d.getStatut()))
                        .toList());
        return "admin/dashboard";
    }

    @GetMapping("/demandes")
    public String demandes(Model model) {
        model.addAttribute("demandes", agentRequestService.findAll());
        return "admin/demandes";
    }

    @PostMapping("/demandes/{id}/approuver")
    public String approuver(@PathVariable Long id,
                            @RequestParam(defaultValue = "") String commentaire) {
        agentRequestService.approuver(id, commentaire);
        return "redirect:/admin/demandes";
    }

    @PostMapping("/demandes/{id}/rejeter")
    public String rejeter(@PathVariable Long id,
                          @RequestParam(defaultValue = "") String commentaire) {
        agentRequestService.rejeter(id, commentaire);
        return "redirect:/admin/demandes";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/activite")
    public String activite(Model model) {
        model.addAttribute("demandes", agentRequestService.findAll());
        model.addAttribute("trajetsAujourdhui",
                trajetRepository.findByDateTrajet(LocalDate.now()));
        model.addAttribute("totalLignes", ligneRepository.count());
        model.addAttribute("totalVehicules", vehiculeRepository.count());
        return "admin/activite";
    }
}