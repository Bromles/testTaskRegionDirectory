package com.bromles.testTaskForTelda.service;

import com.bromles.testTaskForTelda.entity.Region;
import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.repository.IRegionRepository;
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
        regionRepository.addRegion(new Region(regionDTO));
        return regionDTO;
    }

    @Override
    public List<RegionDTO> getAll() {
        List<RegionDTO> regionDTOS= new ArrayList<>();
        List<Region> regions = regionRepository.getRegions();

        for (Region region : regions) {
            regionDTOS.add(new RegionDTO(region));
        }

        return regionDTOS;
    }

    @Override
    public RegionDTO getRegionById(String id) {
        return new RegionDTO(regionRepository.getRegionById(id));
    }

    @Override
    public RegionDTO updateRegionById(String id, RegionDTO regionDTO) {
        return new RegionDTO(regionRepository.updateRegionById(id, new Region(regionDTO)));
    }

    @Override
    public int deleteRegionById(String id) {
        return regionRepository.deleteRegionById(id);
    }
}
