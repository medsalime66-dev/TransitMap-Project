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

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService{

    private final VehiculeRepository vehiculeRepository;
    private final LigneRepository ligneRepository;

    @Override
    public VehiculeDto create(VehiculeDto dto){
        Ligne ligne=ligneRepository.findById(dto.getLigneId()).orElseThrow(()->new ResourceNotFoundException("Ligne introuvable"));
        Vehicule v=Vehicule.builder()
                .matricule(dto.getMatricule())
                .capacite(dto.getCapacite())
                .statut(dto.getStatut())
                .currentLatitude(dto.getCurrentLatitude())
                .currentLongitude(dto.getCurrentLongitude())
                .ligne(ligne)
                .build();
        return map(vehiculeRepository.save(v));
    }

    @Override
    public VehiculeDto update(Long id,VehiculeDto dto){
        Vehicule v=vehiculeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Vehicule introuvable"));
        Ligne ligne=ligneRepository.findById(dto.getLigneId()).orElseThrow(()->new ResourceNotFoundException("Ligne introuvable"));
        v.setMatricule(dto.getMatricule());
        v.setCapacite(dto.getCapacite());
        v.setStatut(dto.getStatut());
        v.setCurrentLatitude(dto.getCurrentLatitude());
        v.setCurrentLongitude(dto.getCurrentLongitude());
        v.setLigne(ligne);
        return map(vehiculeRepository.save(v));
    }

    @Override
    public VehiculeDto findById(Long id){
        return map(vehiculeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Vehicule introuvable")));
    }

    @Override
    public List<VehiculeDto> findAll(){
        return vehiculeRepository.findAll().stream().map(this::map).toList();
    }

    @Override
    public void delete(Long id){
        vehiculeRepository.deleteById(id);
    }

    private VehiculeDto map(Vehicule v){
        return VehiculeDto.builder()
                .id(v.getId())
                .matricule(v.getMatricule())
                .capacite(v.getCapacite())
                .statut(v.getStatut())
                .currentLatitude(v.getCurrentLatitude())
                .currentLongitude(v.getCurrentLongitude())
                .ligneId(v.getLigne().getId())
                .build();
    }
}