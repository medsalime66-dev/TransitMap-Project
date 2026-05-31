package com.transitmap.controller;

import com.transitmap.dto.AgentRequestDto;
import com.transitmap.dto.ArretDto;
import com.transitmap.dto.TrajetDto;
import com.transitmap.repository.LigneRepository;
import com.transitmap.service.AgentRequestService;
import com.transitmap.service.ArretService;
import com.transitmap.service.TrajetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentRequestService agentRequestService;
    private final ArretService arretService;
    private final TrajetService trajetService;
    private final LigneRepository ligneRepository;

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        var demandes = agentRequestService.findByAgent(principal.getName());
        model.addAttribute("demandes", demandes);
        model.addAttribute("total", demandes.size());
        model.addAttribute("enAttente", demandes.stream()
                .filter(d -> "EN_ATTENTE".equals(d.getStatut())).count());
        model.addAttribute("approuve", demandes.stream()
                .filter(d -> "APPROUVE".equals(d.getStatut())).count());
        model.addAttribute("rejete", demandes.stream()
                .filter(d -> "REJETE".equals(d.getStatut())).count());
        return "agent/dashboard";
    }

    // Demandes
    @GetMapping("/demandes")
    public String demandes(Model model, Principal principal) {
        model.addAttribute("demandes",
                agentRequestService.findByAgent(principal.getName()));
        return "agent/demandes";
    }

    @GetMapping("/demandes/create")
    public String createDemandeForm(Model model) {
        model.addAttribute("demande", new AgentRequestDto());
        return "agent/demande-create";
    }

    @PostMapping("/demandes/create")
    public String createDemande(@Valid @ModelAttribute("demande") AgentRequestDto dto,
                                 BindingResult result,
                                 Principal principal) {
        if (result.hasErrors()) return "agent/demande-create";
        agentRequestService.create(dto, principal.getName());
        return "redirect:/agent/demandes";
    }

    // Arrets
    @GetMapping("/arrets/create")
    public String createArretForm(Model model) {
        model.addAttribute("arret", new ArretDto());
        model.addAttribute("lignes", ligneRepository.findAll());
        return "agent/arret-create";
    }

    @PostMapping("/arrets/create")
    public String createArret(@Valid @ModelAttribute("arret") ArretDto dto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("lignes", ligneRepository.findAll());
            return "agent/arret-create";
        }
        arretService.create(dto);
        return "redirect:/agent/dashboard";
    }

    // Trajets
    @GetMapping("/trajets/create")
    public String createTrajetForm(Model model) {
        model.addAttribute("trajet", new TrajetDto());
        model.addAttribute("lignes", ligneRepository.findAll());
        return "agent/trajet-create";
    }

    @PostMapping("/trajets/create")
    public String createTrajet(@Valid @ModelAttribute("trajet") TrajetDto dto,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("lignes", ligneRepository.findAll());
            return "agent/trajet-create";
        }
        trajetService.create(dto);
        return "redirect:/agent/dashboard";
    }
}