package com.bromles.testTaskForTelda.service;

import com.bromles.testTaskForTelda.entity.RegionDTO;

import java.util.List;

public interface IRegionDirectoryService {

    RegionDTO addRegion(RegionDTO regionDTO);

    List<RegionDTO> getAll();

    RegionDTO getRegionById(String id);

    List<RegionDTO> getRegionByName(String name);

    int updateRegionById(String id, RegionDTO regionDTO);

    int deleteRegionById(String id);
}
