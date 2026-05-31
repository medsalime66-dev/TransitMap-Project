package com.transitmap.service;

import com.transitmap.dto.TrajetDto;
import java.util.List;

public interface TrajetService{
    TrajetDto create(TrajetDto dto);
    TrajetDto update(Long id,TrajetDto dto);
    TrajetDto findById(Long id);
    List<TrajetDto> findAll();
    void delete(Long id);
}