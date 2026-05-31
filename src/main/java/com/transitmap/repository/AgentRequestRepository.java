package com.transitmap.repository;

import com.transitmap.entity.AgentRequest;
import com.transitmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgentRequestRepository
        extends JpaRepository<AgentRequest, Long> {

    List<AgentRequest> findByAgentOrderByDateCreationDesc(User agent);

    List<AgentRequest> findByStatutOrderByDateCreationDesc(String statut);

    List<AgentRequest> findAllByOrderByDateCreationDesc();
}