package com.bromles.test_task_region_directory.service;

import com.bromles.test_task_region_directory.entity.RegionDTO;
import com.bromles.test_task_region_directory.exception.DuplicateUniqueValuesException;
import com.bromles.test_task_region_directory.exception.RecordNotFoundException;

import java.util.List;

public interface IRegionDirectoryService {

    /**
     * @param regionDTO interface
     * @throws DuplicateUniqueValuesException
     */
    void add(RegionDTO regionDTO) throws DuplicateUniqueValuesException;

    /**
     * @return
     * @throws RecordNotFoundException
     */
    List<RegionDTO> getAll() throws RecordNotFoundException;

    /**
     * @param id
     * @return
     * @throws RecordNotFoundException
     */
    RegionDTO getById(String id) throws RecordNotFoundException;

    /**
     * @param name
     * @return
     * @throws RecordNotFoundException
     */
    List<RegionDTO> getByName(String name) throws RecordNotFoundException;

    /**
     * @param nameBeginning
     * @return
     * @throws RecordNotFoundException
     */
    List<RegionDTO> getByNameBeginning(String nameBeginning) throws RecordNotFoundException;

    /**
     * @param shortName
     * @return
     * @throws RecordNotFoundException
     */
    List<RegionDTO> getByShortName(String shortName) throws RecordNotFoundException;

    /**
     * @param id
     * @param regionDTO
     * @throws RecordNotFoundException
     * @throws DuplicateUniqueValuesException
     */
    void updateById(String id, RegionDTO regionDTO) throws RecordNotFoundException, DuplicateUniqueValuesException;

    /**
     * @param id
     * @throws RecordNotFoundException
     */
    void deleteById(String id) throws RecordNotFoundException;
}
