package com.transitmap.service.agent;

import com.transitmap.dto.LigneInterurbaineDto;
import com.transitmap.dto.VilleEtapeDto;

import java.util.List;
import java.util.Map;

public interface AgentInterurbainService {

    List<LigneInterurbaineDto> listerLignes(String username);

    LigneInterurbaineDto trouverParId(Long id, String username);

    LigneInterurbaineDto creerLigne(String nom,
                                    String villeDepart, Double latDepart, Double lngDepart,
                                    String villeArrivee, Double latArrivee, Double lngArrivee,
                                    Double prixDirect, String username);

    VilleEtapeDto ajouterArret(Long ligneId,
                                String nomVille, Double latitude, Double longitude,
                                Map<Long, Double> prixParArret, String username);

    void supprimerLigne(Long id, String username);
}
