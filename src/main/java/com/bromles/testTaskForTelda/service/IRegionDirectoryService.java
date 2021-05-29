package com.bromles.testTaskForTelda.service;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.exception.DuplicateUniqueValuesException;
import com.bromles.testTaskForTelda.exception.RecordNotFoundException;

import java.util.List;

public interface IRegionDirectoryService {

    void add(RegionDTO regionDTO) throws DuplicateUniqueValuesException;

    List<RegionDTO> getAll() throws RecordNotFoundException;

    RegionDTO getById(String id) throws RecordNotFoundException;

    List<RegionDTO> getByName(String name) throws RecordNotFoundException;

    List<RegionDTO> getByNameBeginning(String nameBeginning) throws RecordNotFoundException;

    List<RegionDTO> getByShortName(String shortName) throws RecordNotFoundException;

    void updateById(String id, RegionDTO regionDTO) throws RecordNotFoundException;

    void deleteById(String id) throws RecordNotFoundException;
}
