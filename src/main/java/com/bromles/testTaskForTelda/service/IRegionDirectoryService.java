package com.bromles.testTaskForTelda.service;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.exception.DuplicateUniqueValuesException;

import java.util.List;

public interface IRegionDirectoryService {

    RegionDTO add(RegionDTO regionDTO) throws DuplicateUniqueValuesException;

    List<RegionDTO> getAll();

    RegionDTO getById(String id);

    List<RegionDTO> getByName(String name);

    List<RegionDTO> getByShortName(String shortName);

    int updateById(String id, RegionDTO regionDTO);

    int deleteById(String id);
}
