package com.transitmap.service.impl;

import com.transitmap.dto.VehiculeDto;
import com.transitmap.entity.Ligne;
import com.transitmap.entity.Vehicule;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.LigneRepository;
import com.transitmap.repository.VehiculeRepository;
import com.transitmap.service.VehiculeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implémentation du service de gestion des véhicules (usage général).
 */
@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final LigneRepository ligneRepository;

    @Override
    public VehiculeDto create(VehiculeDto dto) {
        Ligne ligne = ligneRepository.findById(dto.getLigneId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ligne introuvable"));

        Vehicule v = Vehicule.builder()
                .matricule(dto.getMatricule())
                .marque(dto.getMarque())
                .capacite(dto.getCapacite())
                .placesDisponibles(dto.getCapacite())
                .annee(dto.getAnnee())
                .statut(dto.getStatut())
                .latitudeActuelle(dto.getLatitudeActuelle())
                .longitudeActuelle(dto.getLongitudeActuelle())
                .ligne(ligne)
                .build();

        return mapper(vehiculeRepository.save(v));
    }

    @Override
    public VehiculeDto update(Long id, VehiculeDto dto) {
        Vehicule v = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable"));
        Ligne ligne = ligneRepository.findById(dto.getLigneId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ligne introuvable"));

        v.setMatricule(dto.getMatricule());
        v.setMarque(dto.getMarque());
        v.setCapacite(dto.getCapacite());
        v.setAnnee(dto.getAnnee());
        v.setStatut(dto.getStatut());
        v.setLatitudeActuelle(dto.getLatitudeActuelle());
        v.setLongitudeActuelle(dto.getLongitudeActuelle());
        v.setLigne(ligne);

        return mapper(vehiculeRepository.save(v));
    }

    @Override
    public VehiculeDto findById(Long id) {
        return mapper(vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable")));
    }

    @Override
    public List<VehiculeDto> findAll() {
        return vehiculeRepository.findAll()
                .stream().map(this::mapper).toList();
    }

    @Override
    public void delete(Long id) {
        vehiculeRepository.deleteById(id);
    }

    /** Convertit Vehicule en DTO */
    private VehiculeDto mapper(Vehicule v) {
        return VehiculeDto.builder()
                .id(v.getId())
                .matricule(v.getMatricule())
                .marque(v.getMarque())
                .capacite(v.getCapacite())
                .placesDisponibles(v.getPlacesDisponibles())
                .annee(v.getAnnee())
                .statut(v.getStatut())
                .latitudeActuelle(v.getLatitudeActuelle())
                .longitudeActuelle(v.getLongitudeActuelle())
                .ligneId(v.getLigne().getId())
                .build();
    }
}