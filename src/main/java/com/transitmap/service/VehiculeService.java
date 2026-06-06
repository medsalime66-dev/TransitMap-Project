package com.transitmap.service;

import com.transitmap.dto.VehiculeDto;
import java.util.List;

public interface VehiculeService{
    VehiculeDto create(VehiculeDto dto);
    VehiculeDto update(Long id,VehiculeDto dto);
    VehiculeDto findById(Long id);
    List<VehiculeDto> findAll();
    void delete(Long id);
}