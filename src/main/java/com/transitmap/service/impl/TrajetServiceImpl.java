package com.transitmap.service.impl;

import com.transitmap.dto.TrajetDto;
import com.transitmap.entity.Ligne;
import com.transitmap.entity.Trajet;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.LigneRepository;
import com.transitmap.repository.TrajetRepository;
import com.transitmap.service.TrajetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrajetServiceImpl implements TrajetService{

    private final TrajetRepository trajetRepository;
    private final LigneRepository ligneRepository;

    @Override
    public TrajetDto create(TrajetDto dto){
        Ligne ligne=ligneRepository.findById(dto.getLigneId()).orElseThrow(()->new ResourceNotFoundException("Ligne introuvable"));
        Trajet t=Trajet.builder().dateTrajet(dto.getDateTrajet()).heureDepart(dto.getHeureDepart()).heureArrivee(dto.getHeureArrivee()).statut(dto.getStatut()).ligne(ligne).build();
        return map(trajetRepository.save(t));
    }

    @Override
    public TrajetDto update(Long id,TrajetDto dto){
        Trajet t=trajetRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Trajet introuvable"));
        Ligne ligne=ligneRepository.findById(dto.getLigneId()).orElseThrow(()->new ResourceNotFoundException("Ligne introuvable"));
        t.setDateTrajet(dto.getDateTrajet());
        t.setHeureDepart(dto.getHeureDepart());
        t.setHeureArrivee(dto.getHeureArrivee());
        t.setStatut(dto.getStatut());
        t.setLigne(ligne);
        return map(trajetRepository.save(t));
    }

    @Override
    public TrajetDto findById(Long id){
        return map(trajetRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Trajet introuvable")));
    }

    @Override
    public List<TrajetDto> findAll(){
        return trajetRepository.findAll().stream().map(this::map).toList();
    }

    @Override
    public void delete(Long id){
        trajetRepository.deleteById(id);
    }

    private TrajetDto map(Trajet t){
        return TrajetDto.builder()
                .id(t.getId())
                .dateTrajet(t.getDateTrajet())
                .heureDepart(t.getHeureDepart())
                .heureArrivee(t.getHeureArrivee())
                .statut(t.getStatut())
                .ligneId(t.getLigne().getId())
                .build();
    }
}