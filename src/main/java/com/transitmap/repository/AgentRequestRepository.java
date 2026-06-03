package com.transitmap.repository;

import com.transitmap.entity.DemandeInscription;
import com.transitmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgentRequestRepository
        extends JpaRepository<DemandeInscription, Long> {

    List<DemandeInscription> findByAgentOrderByDateCreationDesc(User agent);

    List<DemandeInscription> findByStatutOrderByDateCreationDesc(String statut);

    List<DemandeInscription> findAllByOrderByDateCreationDesc();
}