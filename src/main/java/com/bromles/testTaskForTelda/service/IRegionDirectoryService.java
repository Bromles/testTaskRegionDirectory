package com.bromles.testTaskForTelda.service;

import com.bromles.testTaskForTelda.entity.Region;

import java.util.List;

public interface IRegionDirectoryService {

    int addRegion(Region region);

    List<Region> getAll();

    Region getRegionById(Integer id);

    int updateRegionById(Integer id, Region region);

    int deleteRegionById(Integer id);
}
