package com.transitmap.controller.admin;

import com.transitmap.dto.HoraireInterurbainDto;
import com.transitmap.dto.LigneInterurbaineDto;
import com.transitmap.dto.VilleEtapeDto;
import com.transitmap.service.InterurbainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur admin pour la gestion des lignes interurbaines.
 */
@Controller
@RequestMapping("/admin/interurbain")
@RequiredArgsConstructor
public class AdminInterurbainController {

    private final InterurbainService interurbainService;

    /** Liste toutes les lignes interurbaines */
    @GetMapping
    public String liste(Model model) {
        model.addAttribute("lignes",
                interurbainService.findAllLignes());
        return "admin/interurbain/list";
    }

    /** Formulaire de création d'une ligne */
    @GetMapping("/create")
    public String creerForm(Model model) {
        model.addAttribute("ligne", new LigneInterurbaineDto());
        return "admin/interurbain/create";
    }

    /** Traite la création d'une ligne */
    @PostMapping("/create")
    public String creer(
            @Valid @ModelAttribute("ligne") LigneInterurbaineDto dto,
            BindingResult result) {
        if (result.hasErrors()) return "admin/interurbain/create";
        var saved = interurbainService.createLigne(dto);
        return "redirect:/admin/interurbain/" + saved.getId();
    }

    /** Détail d'une ligne avec horaires et étapes */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("ligne",
                interurbainService.findLigneById(id));
        model.addAttribute("horaire", new HoraireInterurbainDto());
        model.addAttribute("etape", new VilleEtapeDto());
        return "admin/interurbain/detail";
    }

    /** Supprime une ligne interurbaine */
    @GetMapping("/{id}/delete")
    public String supprimer(@PathVariable Long id) {
        interurbainService.deleteLigne(id);
        return "redirect:/admin/interurbain";
    }

    /** Ajoute un horaire à une ligne interurbaine */
    @PostMapping("/{id}/horaires/add")
    public String ajouterHoraire(
            @PathVariable Long id,
            @ModelAttribute HoraireInterurbainDto dto) {
        dto.setLigneId(id);
        interurbainService.addHoraire(dto);
        return "redirect:/admin/interurbain/" + id;
    }

    /** Supprime un horaire */
    @GetMapping("/horaires/{id}/delete")
    public String supprimerHoraire(
            @PathVariable Long id,
            @RequestParam Long ligneId) {
        interurbainService.deleteHoraire(id);
        return "redirect:/admin/interurbain/" + ligneId;
    }

    /** Ajoute une ville étape à une ligne interurbaine */
    @PostMapping("/{id}/etapes/add")
    public String ajouterEtape(
            @PathVariable Long id,
            @ModelAttribute VilleEtapeDto dto) {
        dto.setLigneId(id);
        interurbainService.addEtape(dto);
        return "redirect:/admin/interurbain/" + id;
    }

    /** Supprime une ville étape */
    @GetMapping("/etapes/{id}/delete")
    public String supprimerEtape(
            @PathVariable Long id,
            @RequestParam Long ligneId) {
        interurbainService.deleteEtape(id);
        return "redirect:/admin/interurbain/" + ligneId;
    }
}