package com.transitmap.service;

import com.transitmap.dto.ArretDto;
import java.util.List;

public interface ArretService{
    ArretDto create(ArretDto dto);
    ArretDto update(Long id,ArretDto dto);
    ArretDto findById(Long id);
    List<ArretDto> findAll();
    void delete(Long id);
}