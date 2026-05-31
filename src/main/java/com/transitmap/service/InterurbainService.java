package com.transitmap.service;

import com.transitmap.dto.*;
import java.util.List;

public interface InterurbainService {
    LigneInterurbaineDto createLigne(LigneInterurbaineDto dto);
    LigneInterurbaineDto updateLigne(Long id, LigneInterurbaineDto dto);
    LigneInterurbaineDto findLigneById(Long id);
    List<LigneInterurbaineDto> findAllLignes();
    void deleteLigne(Long id);

    HoraireInterurbainDto addHoraire(HoraireInterurbainDto dto);
    HoraireInterurbainDto updateHoraire(Long id, HoraireInterurbainDto dto);
    void deleteHoraire(Long id);

    VilleEtapeDto addEtape(VilleEtapeDto dto);
    void deleteEtape(Long id);

    List<LigneInterurbaineDto> search(String villeDepart, String villeArrivee);
}