package com.transitmap.service.impl;

import com.transitmap.dto.LigneDto;

import com.transitmap.entity.Ligne;

import com.transitmap.exception.ResourceNotFoundException;

import com.transitmap.repository.LigneRepository;

import com.transitmap.service.LigneService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LigneServiceImpl
        implements LigneService {

    private final LigneRepository ligneRepository;

    @Override
    public LigneDto create(
            LigneDto dto
    ) {

        if (ligneRepository.existsByNumero(
                dto.getNumero()
        )) {

            throw new RuntimeException(
                    "Numero already exists"
            );
        }

        Ligne ligne = Ligne.builder()

                .numero(dto.getNumero())

                .nom(dto.getNom())

                .description(dto.getDescription())

                .build();

        return mapToDto(
                ligneRepository.save(ligne)
        );
    }

    @Override
    public LigneDto update(
            Long id,
            LigneDto dto
    ) {

        Ligne ligne =
                ligneRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Ligne not found"
                                )
                        );

        ligne.setNumero(
                dto.getNumero()
        );

        ligne.setNom(
                dto.getNom()
        );

        ligne.setDescription(
                dto.getDescription()
        );

        return mapToDto(
                ligneRepository.save(ligne)
        );
    }

    @Override
    public LigneDto findById(
            Long id
    ) {

        Ligne ligne =
                ligneRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Ligne not found"
                                )
                        );

        return mapToDto(ligne);
    }

    @Override
    public void delete(
            Long id
    ) {

        Ligne ligne =
                ligneRepository.findById(id)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Ligne not found"
                                )
                        );

        ligneRepository.delete(ligne);
    }

    @Override
    public Page<LigneDto> search(

            String keyword,

            int page,

            int size,

            String sortBy
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortBy)
                );

        Page<Ligne> lignes;

        if (
                keyword == null ||
                keyword.isBlank()
        ) {

            lignes =
                    ligneRepository.findAll(
                            pageable
                    );

        } else {

            lignes =
                    ligneRepository
                            .findByNomContainingIgnoreCase(
                                    keyword,
                                    pageable
                            );
        }

        return lignes.map(
                this::mapToDto
        );
    }

    private LigneDto mapToDto(
            Ligne ligne
    ) {

        return LigneDto.builder()

                .id(
                        ligne.getId()
                )

                .numero(
                        ligne.getNumero()
                )

                .nom(
                        ligne.getNom()
                )

                .description(
                        ligne.getDescription()
                )

                .build();
    }
}