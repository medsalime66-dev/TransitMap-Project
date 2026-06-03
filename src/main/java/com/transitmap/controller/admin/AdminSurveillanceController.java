package com.transitmap.controller.admin;

import com.transitmap.repository.*;
import com.transitmap.service.admin.AdminDemandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Contrôleur admin pour la surveillance globale du système.
 * L'administrateur peut tout voir sans modifier.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminSurveillanceController {

    private final LigneRepository ligneRepository;
    private final ArretRepository arretRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetRepository trajetRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final AdminDemandeService adminDemandeService;

    /** Tableau de bord principal de l'admin */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalLignes", ligneRepository.count());
        model.addAttribute("totalArrets", arretRepository.count());
        model.addAttribute("totalVehicules", vehiculeRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("trajetsAujourdhui",
                trajetRepository.findByDateTrajet(LocalDate.now()));
        model.addAttribute("demandesEnAttente",
                adminDemandeService.trouverEnAttente());
        model.addAttribute("totalReservations",
                reservationRepository.count());
        return "admin/dashboard";
    }

    /** Vue de toutes les lignes */
    @GetMapping("/surveillance/lignes")
    public String lignes(Model model) {
        model.addAttribute("lignes", ligneRepository.findAll());
        return "admin/surveillance/lignes";
    }

    /** Vue de tous les véhicules */
    @GetMapping("/surveillance/vehicules")
    public String vehicules(Model model) {
        model.addAttribute("vehicules", vehiculeRepository.findAll());
        return "admin/surveillance/vehicules";
    }

    /** Vue de toutes les réservations */
    @GetMapping("/surveillance/reservations")
    public String reservations(Model model) {
        model.addAttribute("reservations", reservationRepository.findAll());
        return "admin/surveillance/reservations";
    }

    /** Vue de tous les utilisateurs */
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    /** Page d'activité globale */
    @GetMapping("/activite")
    public String activite(Model model) {
        var demandes = adminDemandeService.trouverToutes();
        model.addAttribute("demandes", demandes);
        model.addAttribute("trajetsAujourdhui",
                trajetRepository.findByDateTrajet(LocalDate.now()));
        model.addAttribute("totalLignes", ligneRepository.count());
        model.addAttribute("totalVehicules", vehiculeRepository.count());
        model.addAttribute("demandesEnAttente",
                demandes.stream()
                        .filter(d -> "EN_ATTENTE".equals(
                                d.getStatut() != null
                                        ? d.getStatut().name() : ""))
                        .count());
        return "admin/activite";
    }
}