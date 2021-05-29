package com.bromles.testTaskForTelda.service;

import com.bromles.testTaskForTelda.entity.Region;
import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.exception.DuplicateUniqueValuesException;
import com.bromles.testTaskForTelda.repository.IRegionRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegionDirectoryService implements IRegionDirectoryService {

    private final IRegionRepository regionRepository;

    RegionDirectoryService(IRegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public RegionDTO add(RegionDTO regionDTO) throws DuplicateUniqueValuesException {
        try {
            regionRepository.save(new Region(regionDTO));
            return regionDTO;
        } catch (DuplicateKeyException ex) {
            Map<String, Object> violatedFields = new LinkedHashMap<>();

            violatedFields.put("id", regionDTO.id);

            throw new DuplicateUniqueValuesException(violatedFields);
        }
    }

    @Override
    public List<RegionDTO> getAll() {
        List<RegionDTO> regionDTOs = new ArrayList<>();
        List<Region> regions = regionRepository.getAll();

        for (Region region : regions) {
            regionDTOs.add(new RegionDTO(region));
        }

        return regionDTOs;
    }

    @Override
    public RegionDTO getById(String id) {
        Region region = regionRepository.getById(id);

        if (region != null) {
            return new RegionDTO(region);
        }
        else {
            return null;
        }
    }

    @Override
    public List<RegionDTO> getByName(String name) {
        List<RegionDTO> regionDTOs = new ArrayList<>();
        List<Region> regions = regionRepository.getByName(name);

        for (Region region : regions) {
            regionDTOs.add(new RegionDTO(region));
        }

        return regionDTOs;
    }

    @Override
    public List<RegionDTO> getByShortName(String shortName) {
        List<RegionDTO> regionDTOs = new ArrayList<>();
        List<Region> regions = regionRepository.getByShortName(shortName);

        for (Region region : regions) {
            regionDTOs.add(new RegionDTO(region));
        }

        return regionDTOs;
    }

    @Override
    public int updateById(String id, RegionDTO regionDTO) {
        return regionRepository.updateById(id, new Region(regionDTO));
    }

    @Override
    public int deleteById(String id) {
        return regionRepository.deleteById(id);
    }
}
