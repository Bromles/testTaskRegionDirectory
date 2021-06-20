package com.bromles.test_task_region_directory.service;

import com.bromles.test_task_region_directory.entity.Region;
import com.bromles.test_task_region_directory.entity.RegionDTO;
import com.bromles.test_task_region_directory.exception.DuplicateUniqueValuesException;
import com.bromles.test_task_region_directory.exception.RecordNotFoundException;
import com.bromles.test_task_region_directory.repository.IRegionRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = {"regionDTOsById"})
public class RegionDirectoryService implements IRegionDirectoryService {

    private final IRegionRepository regionRepository;

    RegionDirectoryService(IRegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    @CachePut(key = "#root.args[0].id")
    public void add(RegionDTO regionDTO) throws DuplicateUniqueValuesException {
        try {
            regionRepository.save(new Region(regionDTO));
        }
        catch (DuplicateKeyException ex) {
            Map<String, Object> violatedFields = new LinkedHashMap<>();

            violatedFields.put("id", regionDTO.id);

            throw new DuplicateUniqueValuesException(violatedFields);
        }
    }

    @Override
    public List<RegionDTO> getAll() throws RecordNotFoundException {

        List<Region> regions = regionRepository.getAll();

        return convertRegionsToDTOs(regions);
    }

    @Override
    @Cacheable
    public RegionDTO getById(String id) throws RecordNotFoundException {
        Region region = regionRepository.getById(id);

        if (region != null) {
            return region.toDTO();
        }
        else {
            throw new RecordNotFoundException("id = '" + id + "'");
        }
    }

    @Override
    public List<RegionDTO> getByName(String name) throws RecordNotFoundException {
        List<Region> regions = regionRepository.getByName(name);

        return convertRegionsToDTOs(regions, "name = '" + name + "'");
    }

    @Override
    public List<RegionDTO> getByNameBeginning(String nameBeginning) throws RecordNotFoundException {
        List<Region> regions = regionRepository.getByNameBeginning(nameBeginning);

        return convertRegionsToDTOs(regions, "name beginning = '" + nameBeginning + "'");
    }

    @Override
    public List<RegionDTO> getByShortName(String shortName) throws RecordNotFoundException {
        List<Region> regions = regionRepository.getByShortName(shortName);

        return convertRegionsToDTOs(regions, "short name = '" + shortName + "'");
    }

    @Override
    @CachePut(key = "#root.args[0]")
    public void updateById(String id, RegionDTO regionDTO) throws RecordNotFoundException, DuplicateUniqueValuesException {
        int updatedRows;

        try {
            updatedRows = regionRepository.updateById(id, new Region(regionDTO));
        }
        catch (DuplicateKeyException ex) {
            Map<String, Object> violatedFields = new LinkedHashMap<>();

            violatedFields.put("id", regionDTO.id);

            throw new DuplicateUniqueValuesException(violatedFields);
        }

        if (updatedRows == 0) {
            throw new RecordNotFoundException("id = '" + id + "'");
        }
    }

    @Override
    @CacheEvict(key = "#root.args[0]")
    public void deleteById(String id) throws RecordNotFoundException {
        int deletedRows = regionRepository.deleteById(id);

        if (deletedRows == 0) {
            throw new RecordNotFoundException("id = '" + id + "'");
        }
    }

    private List<RegionDTO> convertRegionsToDTOs(List<Region> regions, String... params) throws RecordNotFoundException {
        if (!regions.isEmpty()) {
            List<RegionDTO> regionDTOs = new ArrayList<>();

            for (Region region : regions) {
                RegionDTO regionDTO = buildRegionDTOAndCacheIt(region);

                regionDTOs.add(regionDTO);
            }

            return regionDTOs;
        }
        else {
            switch (params.length) {
                case 0: {
                    throw new RecordNotFoundException();
                }
                case 1: {
                    throw new RecordNotFoundException(params[0]);
                }
                case 2: {
                    throw new RecordNotFoundException(params[0] + " and " + params[1]);
                }
                default: {
                    throw new IllegalArgumentException("You can pass only 0, 1 or 2 field names");
                }
            }
        }
    }

    @Cacheable(key = "#root.args[0].id")
    public RegionDTO buildRegionDTOAndCacheIt(Region region) {
        return region.toDTO();
    }
}
