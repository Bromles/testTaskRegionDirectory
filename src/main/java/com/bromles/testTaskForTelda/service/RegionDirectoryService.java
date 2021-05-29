package com.bromles.testTaskForTelda.service;

import com.bromles.testTaskForTelda.entity.Region;
import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.repository.IRegionRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegionDirectoryService implements IRegionDirectoryService {

    private final IRegionRepository regionRepository;

    RegionDirectoryService(IRegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public RegionDTO addRegion(RegionDTO regionDTO) {
        try {
            regionRepository.addRegion(new Region(regionDTO));
            return regionDTO;
        } catch (DuplicateKeyException ex) {
            return null;
        }
    }

    @Override
    public List<RegionDTO> getAll() {
        List<RegionDTO> regionDTOs = new ArrayList<>();
        List<Region> regions = regionRepository.getRegions();

        for (Region region : regions) {
            regionDTOs.add(new RegionDTO(region));
        }

        return regionDTOs;
    }

    @Override
    public RegionDTO getRegionById(String id) {
        Region region = regionRepository.getRegionById(id);

        if (region != null) {
            return new RegionDTO(region);
        }
        else {
            return null;
        }
    }

    @Override
    public List<RegionDTO> getRegionByName(String name) {
        List<RegionDTO> regionDTOs = new ArrayList<>();
        List<Region> regions = regionRepository.getRegionByName(name);

        for (Region region : regions) {
            regionDTOs.add(new RegionDTO(region));
        }

        return regionDTOs;
    }

    @Override
    public RegionDTO updateRegionById(String id, RegionDTO regionDTO) {
        regionRepository.updateRegionById(id, new Region(regionDTO));
        return regionDTO;
    }

    @Override
    public int deleteRegionById(String id) {
        return regionRepository.deleteRegionById(id);
    }
}
