package com.transitmap.service.agent;

import com.transitmap.dto.LigneInterurbaineDto;
import com.transitmap.dto.VilleEtapeDto;
import com.transitmap.entity.*;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AgentInterurbainServiceImpl implements AgentInterurbainService {

    private final LigneInterurbaineRepository ligneRepo;
    private final VilleEtapeRepository etapeRepo;
    private final PrixSegmentRepository prixRepo;
    private final UserRepository userRepo;

    @Override
    @Transactional(readOnly = true)
    public List<LigneInterurbaineDto> listerLignes(String username) {
        return ligneRepo.findByCreateurUsername(username)
                .stream().map(this::mapLigne).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LigneInterurbaineDto trouverParId(Long id, String username) {
        return mapLigne(trouverLigneDeLAgent(id, username));
    }

    @Override
    @Transactional
    public LigneInterurbaineDto creerLigne(String nom,
                                            String villeDepart, Double latDepart, Double lngDepart,
                                            String villeArrivee, Double latArrivee, Double lngArrivee,
                                            Double prixDirect, String username) {
        User agent = trouverUser(username);

        LigneInterurbaine ligne = ligneRepo.save(LigneInterurbaine.builder()
                .nom(nom)
                .villeDepart(villeDepart)
                .villeArrivee(villeArrivee)
                .createur(agent)
                .build());

        VilleEtape depart = etapeRepo.save(VilleEtape.builder()
                .ligne(ligne).nomVille(villeDepart)
                .latitude(latDepart).longitude(lngDepart)
                .ordre(1).estDepart(true).estArrivee(false).build());

        VilleEtape arrivee = etapeRepo.save(VilleEtape.builder()
                .ligne(ligne).nomVille(villeArrivee)
                .latitude(latArrivee).longitude(lngArrivee)
                .ordre(2).estDepart(false).estArrivee(true).build());

        prixRepo.save(PrixSegment.builder()
                .ligne(ligne).arretA(depart).arretB(arrivee).prix(prixDirect).build());

        return mapLigne(ligne);
    }

    @Override
    @Transactional
    public VilleEtapeDto ajouterArret(Long ligneId,
                                       String nomVille, Double latitude, Double longitude,
                                       Map<Long, Double> prixParArret, String username) {
        LigneInterurbaine ligne = trouverLigneDeLAgent(ligneId, username);

        int maxOrdre = etapeRepo.findByLigneOrderByOrdreAsc(ligne).stream()
                .mapToInt(VilleEtape::getOrdre).max().orElse(0);

        VilleEtape nouvel = etapeRepo.save(VilleEtape.builder()
                .ligne(ligne).nomVille(nomVille)
                .latitude(latitude).longitude(longitude)
                .ordre(maxOrdre + 1).estDepart(false).estArrivee(false).build());

        for (Map.Entry<Long, Double> entry : prixParArret.entrySet()) {
            VilleEtape existant = etapeRepo.findById(entry.getKey())
                    .orElseThrow(() -> new ResourceNotFoundException("Arrêt introuvable"));
            prixRepo.save(PrixSegment.builder()
                    .ligne(ligne).arretA(nouvel).arretB(existant)
                    .prix(entry.getValue()).build());
        }

        reordonnerEtapes(ligne);

        return mapEtape(etapeRepo.findById(nouvel.getId()).orElse(nouvel));
    }

    @Override
    @Transactional
    public void supprimerLigne(Long id, String username) {
        LigneInterurbaine ligne = trouverLigneDeLAgent(id, username);
        ligneRepo.delete(ligne);
    }

    private LigneInterurbaine trouverLigneDeLAgent(Long id, String username) {
        LigneInterurbaine ligne = ligneRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne introuvable"));
        if (ligne.getCreateur() == null || !ligne.getCreateur().getUsername().equals(username)) {
            throw new RuntimeException("Accès non autorisé à cette ligne");
        }
        return ligne;
    }

    private User trouverUser(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + username));
    }

    private LigneInterurbaineDto mapLigne(LigneInterurbaine l) {
        List<VilleEtapeDto> etapes = etapeRepo.findByLigneOrderByOrdreAsc(l)
                .stream().map(this::mapEtape).toList();
        return LigneInterurbaineDto.builder()
                .id(l.getId())
                .nom(l.getNom())
                .villeDepart(l.getVilleDepart())
                .villeArrivee(l.getVilleArrivee())
                .description(l.getDescription())
                .etapes(etapes)
                .build();
    }

    private void reordonnerEtapes(LigneInterurbaine ligne) {
        List<VilleEtape> toutes = etapeRepo.findByLigneOrderByOrdreAsc(ligne);

        VilleEtape pointDepart  = toutes.stream().filter(VilleEtape::isEstDepart).findFirst().orElse(null);
        VilleEtape pointArrivee = toutes.stream().filter(VilleEtape::isEstArrivee).findFirst().orElse(null);

        if (pointDepart == null || pointArrivee == null) return;

        List<VilleEtape> intermediaires = toutes.stream()
                .filter(e -> !e.isEstDepart() && !e.isEstArrivee())
                .sorted(Comparator.comparingDouble(e ->
                        calculerDistance(pointDepart.getLatitude(), pointDepart.getLongitude(),
                                         e.getLatitude(), e.getLongitude())))
                .toList();

        int ordre = 1;
        pointDepart.setOrdre(ordre++);
        etapeRepo.save(pointDepart);

        for (VilleEtape e : intermediaires) {
            e.setOrdre(ordre++);
            etapeRepo.save(e);
        }

        pointArrivee.setOrdre(ordre);
        etapeRepo.save(pointArrivee);
    }

    private double calculerDistance(double lat1, double lng1, double lat2, double lng2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private VilleEtapeDto mapEtape(VilleEtape e) {
        return VilleEtapeDto.builder()
                .id(e.getId())
                .ligneId(e.getLigne().getId())
                .nomVille(e.getNomVille())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .ordre(e.getOrdre())
                .estDepart(e.isEstDepart())
                .estArrivee(e.isEstArrivee())
                .build();
    }
}
