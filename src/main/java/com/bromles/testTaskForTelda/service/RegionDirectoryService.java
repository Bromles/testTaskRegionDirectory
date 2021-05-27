package com.bromles.testTaskForTelda.service;

import com.bromles.testTaskForTelda.entity.Region;
import com.bromles.testTaskForTelda.repository.IRegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionDirectoryService implements IRegionDirectoryService {

    private final IRegionRepository regionRepository;

    RegionDirectoryService(IRegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public int addRegion(Region region) {
        return regionRepository.addRegion(region);
    }

    @Override
    public List<Region> getAll() {
        return regionRepository.getRegions();
    }

    @Override
    public Region getRegionById(Integer id) {
        return regionRepository.getRegionById(id);
    }

    @Override
    public int updateRegionById(Integer id, Region region) {
        return regionRepository.updateRegionById(id, region);
    }

    @Override
    public int deleteRegionById(Integer id) {
        return regionRepository.deleteRegionById(id);
    }
}
