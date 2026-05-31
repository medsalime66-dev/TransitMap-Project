package com.transitmap.service.impl;

import com.transitmap.dto.AgentRequestDto;
import com.transitmap.entity.AgentRequest;
import com.transitmap.entity.User;
import com.transitmap.exception.ResourceNotFoundException;
import com.transitmap.repository.AgentRequestRepository;
import com.transitmap.repository.UserRepository;
import com.transitmap.service.AgentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentRequestServiceImpl implements AgentRequestService {

    private final AgentRequestRepository agentRequestRepository;
    private final UserRepository userRepository;

    @Override
    public AgentRequestDto create(AgentRequestDto dto, String username) {
        User agent = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        AgentRequest request = AgentRequest.builder()
                .agent(agent)
                .nomLigne(dto.getNomLigne())
                .numeroLigne(dto.getNumeroLigne())
                .description(dto.getDescription())
                .statut("EN_ATTENTE")
                .dateCreation(LocalDateTime.now())
                .build();

        return map(agentRequestRepository.save(request));
    }

    @Override
    public List<AgentRequestDto> findByAgent(String username) {
        User agent = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        return agentRequestRepository
                .findByAgentOrderByDateCreationDesc(agent)
                .stream().map(this::map).toList();
    }

    @Override
    public List<AgentRequestDto> findAll() {
        return agentRequestRepository
                .findAllByOrderByDateCreationDesc()
                .stream().map(this::map).toList();
    }

    @Override
    public AgentRequestDto approuver(Long id, String commentaire) {
        AgentRequest r = agentRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable"));
        r.setStatut("APPROUVE");
        r.setCommentaireAdmin(commentaire);
        r.setDateTraitement(LocalDateTime.now());
        return map(agentRequestRepository.save(r));
    }

    @Override
    public AgentRequestDto rejeter(Long id, String commentaire) {
        AgentRequest r = agentRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable"));
        r.setStatut("REJETE");
        r.setCommentaireAdmin(commentaire);
        r.setDateTraitement(LocalDateTime.now());
        return map(agentRequestRepository.save(r));
    }

    private AgentRequestDto map(AgentRequest r) {
        return AgentRequestDto.builder()
                .id(r.getId())
                .nomLigne(r.getNomLigne())
                .numeroLigne(r.getNumeroLigne())
                .description(r.getDescription())
                .statut(r.getStatut())
                .agentUsername(r.getAgent().getUsername())
                .dateCreation(r.getDateCreation())
                .dateTraitement(r.getDateTraitement())
                .commentaireAdmin(r.getCommentaireAdmin())
                .build();
    }
}