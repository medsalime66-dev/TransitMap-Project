package com.transitmap.service;

import com.transitmap.dto.AgentRequestDto;
import java.util.List;

public interface AgentRequestService {
    AgentRequestDto create(AgentRequestDto dto, String username);
    List<AgentRequestDto> findByAgent(String username);
    List<AgentRequestDto> findAll();
    AgentRequestDto approuver(Long id, String commentaire);
    AgentRequestDto rejeter(Long id, String commentaire);
}