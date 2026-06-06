package com.transitmap.service.impl;

import com.transitmap.dto.ArretDto;
import com.transitmap.entity.Arret;
import com.transitmap.entity.Ligne;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.ArretRepository;
import com.transitmap.repository.LigneRepository;
import com.transitmap.service.ArretService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArretServiceImpl implements ArretService{

    private final ArretRepository arretRepository;
    private final LigneRepository ligneRepository;

    @Override
    public ArretDto create(ArretDto dto){
        Ligne ligne=ligneRepository.findById(dto.getLigneId()).orElseThrow(()->new ResourceNotFoundException("Ligne introuvable"));
        Arret arret=Arret.builder().nom(dto.getNom()).latitude(dto.getLatitude()).longitude(dto.getLongitude()).ordre(dto.getOrdre()).ligne(ligne).build();
        return map(arretRepository.save(arret));
    }

    @Override
    public ArretDto update(Long id,ArretDto dto){
        Arret arret=arretRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Arret introuvable"));
        Ligne ligne=ligneRepository.findById(dto.getLigneId()).orElseThrow(()->new ResourceNotFoundException("Ligne introuvable"));
        arret.setNom(dto.getNom());
        arret.setLatitude(dto.getLatitude());
        arret.setLongitude(dto.getLongitude());
        arret.setOrdre(dto.getOrdre());
        arret.setLigne(ligne);
        return map(arretRepository.save(arret));
    }

    @Override
    public ArretDto findById(Long id){
        return map(arretRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Arret introuvable")));
    }

    @Override
    public List<ArretDto> findAll(){
        return arretRepository.findAll().stream().map(this::map).toList();
    }

    @Override
    public void delete(Long id){
        arretRepository.deleteById(id);
    }

    private ArretDto map(Arret a){
        return ArretDto.builder().id(a.getId()).nom(a.getNom()).latitude(a.getLatitude()).longitude(a.getLongitude()).ordre(a.getOrdre()).ligneId(a.getLigne().getId()).build();
    }
}