package com.transitmap.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agent_requests")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AgentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    @Column(nullable = false, length = 100)
    private String nomLigne;

    @Column(nullable = false, length = 50)
    private String numeroLigne;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 30)
    private String statut; // EN_ATTENTE, APPROUVE, REJETE

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column
    private LocalDateTime dateTraitement;

    @Column(length = 300)
    private String commentaireAdmin;
}