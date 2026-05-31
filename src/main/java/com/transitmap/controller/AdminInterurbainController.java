package com.transitmap.controller;

import com.transitmap.dto.*;
import com.transitmap.service.InterurbainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/interurbain")
@RequiredArgsConstructor
public class AdminInterurbainController {

    private final InterurbainService interurbainService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("lignes", interurbainService.findAllLignes());
        return "admin/interurbain/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("ligne", new LigneInterurbaineDto());
        return "admin/interurbain/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("ligne") LigneInterurbaineDto dto,
                         BindingResult result) {
        if (result.hasErrors()) return "admin/interurbain/create";
        var saved = interurbainService.createLigne(dto);
        return "redirect:/admin/interurbain/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("ligne", interurbainService.findLigneById(id));
        model.addAttribute("horaire", new HoraireInterurbainDto());
        model.addAttribute("etape", new VilleEtapeDto());
        return "admin/interurbain/detail";
    }

    @PostMapping("/{id}/horaires/add")
    public String addHoraire(@PathVariable Long id,
                              @Valid @ModelAttribute("horaire") HoraireInterurbainDto dto,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ligne", interurbainService.findLigneById(id));
            model.addAttribute("etape", new VilleEtapeDto());
            return "admin/interurbain/detail";
        }
        dto.setLigneId(id);
        interurbainService.addHoraire(dto);
        return "redirect:/admin/interurbain/" + id;
    }

    @GetMapping("/horaires/{id}/delete")
    public String deleteHoraire(@PathVariable Long id,
                                 @RequestParam Long ligneId) {
        interurbainService.deleteHoraire(id);
        return "redirect:/admin/interurbain/" + ligneId;
    }

    @PostMapping("/{id}/etapes/add")
    public String addEtape(@PathVariable Long id,
                            @Valid @ModelAttribute("etape") VilleEtapeDto dto,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ligne", interurbainService.findLigneById(id));
            model.addAttribute("horaire", new HoraireInterurbainDto());
            return "admin/interurbain/detail";
        }
        dto.setLigneId(id);
        interurbainService.addEtape(dto);
        return "redirect:/admin/interurbain/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteLigne(@PathVariable Long id) {
        interurbainService.deleteLigne(id);
        return "redirect:/admin/interurbain";
    }
}