package com.transitmap.controller;

import com.transitmap.repository.*;
import com.transitmap.service.admin.AdminDemandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Contrôleur principal de l'administrateur.
 * Gère le tableau de bord, les demandes et la surveillance.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LigneRepository ligneRepository;
    private final ArretRepository arretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetRepository trajetRepository;
    private final UserRepository userRepository;
    private final AdminDemandeService adminDemandeService;

    /** Tableau de bord principal */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        var demandes = adminDemandeService.trouverToutes();
        model.addAttribute("totalLignes", ligneRepository.count());
        model.addAttribute("totalArrets", arretRepository.count());
        model.addAttribute("totalVehicules", vehiculeRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("trajetsAujourdhui",
                trajetRepository.findByDateTrajet(LocalDate.now()));
        model.addAttribute("demandesEnAttente",
                adminDemandeService.trouverEnAttente());
        return "admin/dashboard";
    }

    /** Liste toutes les demandes de lignes */
    @GetMapping("/demandes")
    public String demandes(Model model) {
        model.addAttribute("demandes", adminDemandeService.trouverToutes());
        return "admin/demandes";
    }

    /** Approuve une demande */
    @PostMapping("/demandes/{id}/approuver")
    public String approuver(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Approuvée") String commentaire) {
        adminDemandeService.approuver(id, commentaire);
        return "redirect:/admin/demandes";
    }

    /** Rejette une demande */
    @PostMapping("/demandes/{id}/rejeter")
    public String rejeter(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Rejetée") String commentaire) {
        adminDemandeService.rejeter(id, commentaire);
        return "redirect:/admin/demandes";
    }

    /** Liste tous les utilisateurs */
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    /** Page d'activité globale du système */
    @GetMapping("/activite")
    public String activite(Model model) {
        var demandes = adminDemandeService.trouverToutes();
        model.addAttribute("demandes", demandes);
        model.addAttribute("trajetsAujourdhui",
                trajetRepository.findByDateTrajet(LocalDate.now()));
        model.addAttribute("totalLignes", ligneRepository.count());
        model.addAttribute("totalVehicules", vehiculeRepository.count());
        model.addAttribute("demandesEnAttente",
                adminDemandeService.trouverEnAttente().size());
        return "admin/activite";
    }
}