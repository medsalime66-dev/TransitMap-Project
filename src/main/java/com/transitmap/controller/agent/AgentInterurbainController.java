package com.transitmap.controller.agent;

import com.transitmap.dto.LigneInterurbaineDto;
import com.transitmap.dto.VilleEtapeDto;
import com.transitmap.entity.PrixSegment;
import com.transitmap.repository.PrixSegmentRepository;
import com.transitmap.service.agent.AgentInterurbainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/agent/interurbain")
@RequiredArgsConstructor
public class AgentInterurbainController {

    private final AgentInterurbainService service;
    private final PrixSegmentRepository prixRepo;

    @GetMapping("")
    public String liste(Model model, Principal principal) {
        model.addAttribute("lignes", service.listerLignes(principal.getName()));
        return "agent/interurbain/liste";
    }

    @GetMapping("/creer")
    public String creerForm() {
        return "agent/interurbain/creer";
    }

    @PostMapping("/creer")
    public String creerPost(@RequestParam String nom,
                             @RequestParam String villeDepart,
                             @RequestParam Double latDepart,
                             @RequestParam Double lngDepart,
                             @RequestParam String villeArrivee,
                             @RequestParam Double latArrivee,
                             @RequestParam Double lngArrivee,
                             @RequestParam Double prixDirect,
                             Principal principal) {
        LigneInterurbaineDto dto = service.creerLigne(
                nom, villeDepart, latDepart, lngDepart,
                villeArrivee, latArrivee, lngArrivee, prixDirect,
                principal.getName());
        return "redirect:/agent/interurbain/" + dto.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, Principal principal) {
        LigneInterurbaineDto ligne = service.trouverParId(id, principal.getName());
        List<PrixSegment> segments = prixRepo.findByLigneId(id);

        model.addAttribute("ligne", ligne);
        model.addAttribute("etapesJson", buildEtapesJson(ligne.getEtapes()));
        model.addAttribute("matrice", buildMatrice(ligne.getEtapes(), segments));
        return "agent/interurbain/detail";
    }

    @GetMapping("/{id}/arret/ajouter")
    public String ajouterArretForm(@PathVariable Long id, Model model, Principal principal) {
        LigneInterurbaineDto ligne = service.trouverParId(id, principal.getName());
        model.addAttribute("ligne", ligne);
        model.addAttribute("etapesJson", buildEtapesJson(ligne.getEtapes()));
        return "agent/interurbain/ajouter-arret";
    }

    @PostMapping("/{id}/arret/ajouter")
    public String ajouterArretPost(@PathVariable Long id,
                                    @RequestParam String nomVille,
                                    @RequestParam Double latitude,
                                    @RequestParam Double longitude,
                                    @RequestParam Map<String, String> allParams,
                                    Principal principal) {
        Map<Long, Double> prixParArret = new HashMap<>();
        allParams.forEach((key, value) -> {
            if (key.startsWith("prix_") && !value.isBlank()) {
                try {
                    Long arretId = Long.parseLong(key.substring(5));
                    prixParArret.put(arretId, Double.parseDouble(value));
                } catch (NumberFormatException ignored) {}
            }
        });
        service.ajouterArret(id, nomVille, latitude, longitude, prixParArret, principal.getName());
        return "redirect:/agent/interurbain/" + id;
    }

    @GetMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, Principal principal) {
        service.supprimerLigne(id, principal.getName());
        return "redirect:/agent/interurbain";
    }

    private String buildEtapesJson(List<VilleEtapeDto> etapes) {
        if (etapes == null || etapes.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < etapes.size(); i++) {
            VilleEtapeDto e = etapes.get(i);
            if (i > 0) sb.append(",");
            sb.append("{\"id\":").append(e.getId())
              .append(",\"lat\":").append(e.getLatitude())
              .append(",\"lng\":").append(e.getLongitude())
              .append(",\"nom\":\"").append(escape(e.getNomVille()))
              .append("\",\"ordre\":").append(e.getOrdre()).append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    private List<List<String>> buildMatrice(List<VilleEtapeDto> etapes, List<PrixSegment> segments) {
        Map<String, Double> lookup = new HashMap<>();
        for (PrixSegment seg : segments) {
            Long a = seg.getArretA().getId(), b = seg.getArretB().getId();
            lookup.put(Math.min(a, b) + "|" + Math.max(a, b), seg.getPrix());
        }
        List<List<String>> matrix = new ArrayList<>();
        for (VilleEtapeDto row : etapes) {
            List<String> line = new ArrayList<>();
            for (VilleEtapeDto col : etapes) {
                if (row.getId().equals(col.getId())) {
                    line.add("—");
                } else {
                    Long a = row.getId(), b = col.getId();
                    Double prix = lookup.get(Math.min(a, b) + "|" + Math.max(a, b));
                    line.add(prix != null ? String.format("%.0f", prix) : "N/A");
                }
            }
            matrix.add(line);
        }
        return matrix;
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
