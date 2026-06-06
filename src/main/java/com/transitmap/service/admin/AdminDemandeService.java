package com.transitmap.service.admin;

import com.transitmap.dto.DemandeInscriptionDto;
import java.util.List;

/**
 * Service de traitement des demandes d'inscription par l'administrateur.
 */
public interface AdminDemandeService {

    /** Retourne toutes les demandes triées par date */
    List<DemandeInscriptionDto> trouverToutes();

    /** Retourne les demandes en attente */
    List<DemandeInscriptionDto> trouverEnAttente();

    /** Approuve une demande et crée le compte agent */
    DemandeInscriptionDto approuver(Long id, String commentaire);

    /** Rejette une demande */
    DemandeInscriptionDto rejeter(Long id, String commentaire);

    /** Retourne une demande par son ID */
    DemandeInscriptionDto trouverParId(Long id);
}