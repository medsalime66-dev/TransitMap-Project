package com.transitmap.controller.admin;

import com.transitmap.service.admin.AdminDemandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur admin pour la gestion des demandes d'inscription.
 * L'administrateur peut approuver ou rejeter les demandes.
 */
@Controller
@RequestMapping("/admin/demandes-inscription")
@RequiredArgsConstructor
public class AdminDemandeController {

    private final AdminDemandeService adminDemandeService;

    /** Liste toutes les demandes d'inscription */
    @GetMapping
    public String lister(Model model) {
        model.addAttribute("demandes",
                adminDemandeService.trouverToutes());
        model.addAttribute("enAttente",
                adminDemandeService.trouverEnAttente().size());
        return "admin/demandes-inscription";
    }

    /** Affiche le détail d'une demande */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("demande",
                adminDemandeService.trouverParId(id));
        return "admin/demande-detail";
    }

    /** Approuve une demande et crée le compte agent */
    @PostMapping("/{id}/approuver")
    public String approuver(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Demande approuvée")
            String commentaire) {
        adminDemandeService.approuver(id, commentaire);
        return "redirect:/admin/demandes-inscription";
    }

    /** Rejette une demande */
    @PostMapping("/{id}/rejeter")
    public String rejeter(
            @PathVariable Long id,
            @RequestParam(defaultValue = "Demande rejetée")
            String commentaire) {
        adminDemandeService.rejeter(id, commentaire);
        return "redirect:/admin/demandes-inscription";
    }
}