package com.bromles.test_task_region_directory.service;

import com.bromles.test_task_region_directory.entity.RegionDTO;
import com.bromles.test_task_region_directory.exception.DuplicateUniqueValuesException;
import com.bromles.test_task_region_directory.exception.RecordNotFoundException;

import java.util.List;

public interface IRegionDirectoryService {

    void add(RegionDTO regionDTO) throws DuplicateUniqueValuesException;

    List<RegionDTO> getAll() throws RecordNotFoundException;

    RegionDTO getById(String id) throws RecordNotFoundException;

    List<RegionDTO> getByName(String name) throws RecordNotFoundException;

    List<RegionDTO> getByNameBeginning(String nameBeginning) throws RecordNotFoundException;

    List<RegionDTO> getByShortName(String shortName) throws RecordNotFoundException;

    void updateById(String id, RegionDTO regionDTO) throws RecordNotFoundException, DuplicateUniqueValuesException;

    void deleteById(String id) throws RecordNotFoundException;
}
