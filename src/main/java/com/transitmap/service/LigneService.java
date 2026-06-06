package com.transitmap.service;

import com.transitmap.dto.LigneDto;

import org.springframework.data.domain.Page;

public interface LigneService {

    LigneDto create(
            LigneDto dto
    );

    LigneDto update(
            Long id,
            LigneDto dto
    );

    LigneDto findById(
            Long id
    );

    void delete(
            Long id
    );

    Page<LigneDto> search(
            String keyword,
            int page,
            int size,
            String sortBy
    );
}