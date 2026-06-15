package com.transitmap.controller.admin;

import com.transitmap.repository.*;
import com.transitmap.service.admin.AdminDemandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminSurveillanceController {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final AdminDemandeService adminDemandeService;
    private final LigneInterurbaineRepository ligneInterurbaineRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalLignes", ligneInterurbaineRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("demandesEnAttente", adminDemandeService.trouverEnAttente());
        model.addAttribute("totalReservations", reservationRepository.count());
        model.addAttribute("trajetsAujourdhui", java.util.List.of());
        return "admin/dashboard";
    }

    @GetMapping("/surveillance/vehicules")
    public String vehicules(Model model) {
        return "admin/surveillance/vehicules";
    }

    @GetMapping("/surveillance/reservations")
    public String reservations(Model model) {
        model.addAttribute("reservations", reservationRepository.findAll());
        return "admin/surveillance/reservations";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/activite")
    public String activite(Model model) {
        var demandes = adminDemandeService.trouverToutes();
        model.addAttribute("demandes", demandes);
        model.addAttribute("trajetsAujourdhui", java.util.List.of());
        model.addAttribute("totalLignes", ligneInterurbaineRepository.count());
        model.addAttribute("demandesEnAttente",
                demandes.stream()
                        .filter(d -> d.getStatut() != null &&
                                d.getStatut() == com.transitmap.entity.DemandeInscription.StatutDemande.EN_ATTENTE)
                        .count());
        return "admin/activite";
    }

    @GetMapping("/demandes")
    public String demandes(Model model) {
        model.addAttribute("demandes", adminDemandeService.trouverToutes());
        return "admin/demandes";
    }
}
