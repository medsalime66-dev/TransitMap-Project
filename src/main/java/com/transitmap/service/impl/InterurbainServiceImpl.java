package com.transitmap.service.impl;

import com.transitmap.dto.*;
import com.transitmap.entity.*;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.*;
import com.transitmap.service.InterurbainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterurbainServiceImpl implements InterurbainService {

    private final LigneInterurbaineRepository ligneRepo;
    private final VilleEtapeRepository etapeRepo;
    private final HoraireInterurbaineRepository horaireRepo;

    @Override
    @Transactional
    public LigneInterurbaineDto createLigne(LigneInterurbaineDto dto) {
        LigneInterurbaine ligne = LigneInterurbaine.builder()
                .nom(dto.getNom())
                .villeDepart(dto.getVilleDepart())
                .villeArrivee(dto.getVilleArrivee())
                .description(dto.getDescription())
                .build();
        return mapLigne(ligneRepo.save(ligne));
    }

    @Override
    @Transactional
    public LigneInterurbaineDto updateLigne(Long id, LigneInterurbaineDto dto) {
        LigneInterurbaine ligne = ligneRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne introuvable"));
        ligne.setNom(dto.getNom());
        ligne.setVilleDepart(dto.getVilleDepart());
        ligne.setVilleArrivee(dto.getVilleArrivee());
        ligne.setDescription(dto.getDescription());
        return mapLigne(ligneRepo.save(ligne));
    }

    @Override
    @Transactional(readOnly = true)
    public LigneInterurbaineDto findLigneById(Long id) {
        LigneInterurbaine ligne = ligneRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ligne introuvable"));
        LigneInterurbaineDto dto = mapLigne(ligne);
        dto.setEtapes(etapeRepo.findByLigneOrderByOrdreAsc(ligne)
                .stream().map(this::mapEtape).toList());
        dto.setHoraires(horaireRepo.findByLigneAndActifTrue(ligne)
                .stream().map(this::mapHoraire).toList());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneInterurbaineDto> findAllLignes() {
        return ligneRepo.findAll().stream().map(l -> {
            LigneInterurbaineDto dto = mapLigne(l);
            dto.setHoraires(horaireRepo.findByLigneAndActifTrue(l)
                    .stream().map(this::mapHoraire).toList());
            return dto;
        }).toList();
    }

    @Override
    public void deleteLigne(Long id) {
        ligneRepo.deleteById(id);
    }

    @Override
    @Transactional
    public HoraireInterurbainDto addHoraire(HoraireInterurbainDto dto) {
        LigneInterurbaine ligne = ligneRepo.findById(dto.getLigneId())
                .orElseThrow(() -> new ResourceNotFoundException("Ligne introuvable"));
        HoraireInterurbain h = HoraireInterurbain.builder()
                .ligne(ligne)
                .heureDepart(dto.getHeureDepart())
                .heureArrivee(dto.getHeureArrivee())
                .jours(dto.getJours())
                .prix(dto.getPrix())
                .actif(true)
                .build();
        return mapHoraire(horaireRepo.save(h));
    }

    @Override
    @Transactional
    public HoraireInterurbainDto updateHoraire(Long id, HoraireInterurbainDto dto) {
        HoraireInterurbain h = horaireRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horaire introuvable"));
        h.setHeureDepart(dto.getHeureDepart());
        h.setHeureArrivee(dto.getHeureArrivee());
        h.setJours(dto.getJours());
        h.setPrix(dto.getPrix());
        h.setActif(dto.getActif() != null ? dto.getActif() : true);
        return mapHoraire(horaireRepo.save(h));
    }

    @Override
    public void deleteHoraire(Long id) {
        horaireRepo.deleteById(id);
    }

    @Override
    @Transactional
    public VilleEtapeDto addEtape(VilleEtapeDto dto) {
        LigneInterurbaine ligne = ligneRepo.findById(dto.getLigneId())
                .orElseThrow(() -> new ResourceNotFoundException("Ligne introuvable"));
        VilleEtape e = VilleEtape.builder()
                .ligne(ligne)
                .nomVille(dto.getNomVille())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .ordre(dto.getOrdre())
                .build();
        return mapEtape(etapeRepo.save(e));
    }

    @Override
    public void deleteEtape(Long id) {
        etapeRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneInterurbaineDto> search(String villeDepart, String villeArrivee) {
        return ligneRepo.findByVilleDepartAndVilleArrivee(villeDepart, villeArrivee)
                .stream().map(l -> {
                    LigneInterurbaineDto dto = mapLigne(l);
                    dto.setHoraires(horaireRepo.findByLigneAndActifTrue(l)
                            .stream().map(this::mapHoraire).toList());
                    return dto;
                }).toList();
    }

    private LigneInterurbaineDto mapLigne(LigneInterurbaine l) {
        return LigneInterurbaineDto.builder()
                .id(l.getId())
                .nom(l.getNom())
                .villeDepart(l.getVilleDepart())
                .villeArrivee(l.getVilleArrivee())
                .description(l.getDescription())
                .build();
    }

    private VilleEtapeDto mapEtape(VilleEtape e) {
        return VilleEtapeDto.builder()
                .id(e.getId())
                .ligneId(e.getLigne().getId())
                .nomVille(e.getNomVille())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .ordre(e.getOrdre())
                .build();
    }

    private HoraireInterurbainDto mapHoraire(HoraireInterurbain h) {
        return HoraireInterurbainDto.builder()
                .id(h.getId())
                .ligneId(h.getLigne().getId())
                .heureDepart(h.getHeureDepart())
                .heureArrivee(h.getHeureArrivee())
                .jours(h.getJours())
                .prix(h.getPrix())
                .actif(h.getActif())
                .build();
    }
}
