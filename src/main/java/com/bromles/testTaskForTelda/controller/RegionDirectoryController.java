package com.bromles.testTaskForTelda.controller;

import com.bromles.testTaskForTelda.entity.Region;
import com.bromles.testTaskForTelda.service.IRegionDirectoryService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RegionDirectoryController {

    private final IRegionDirectoryService regionDirectoryService;

    RegionDirectoryController(IRegionDirectoryService regionDirectoryService) {
        this.regionDirectoryService = regionDirectoryService;
        regionDirectoryService.addRegion(new Region(1, "Moskow", "001"));
    }

    @PostMapping("/regions")
    int addRegion(@Valid @RequestBody Region region) {
        return regionDirectoryService.addRegion(region);
    }

    @GetMapping("/regions")
    List<Region> getAll() {
        return regionDirectoryService.getAll();
    }

    @GetMapping("/regions/{id}")
    Region getRegion(@Valid @PathVariable Integer id) {
        return regionDirectoryService.getRegionById(id);
    }

    @PutMapping("/regions/{id}")
    int updateRegion(@Valid @PathVariable Integer id, @Valid @RequestBody Region region) {
        return regionDirectoryService.updateRegionById(id, region);
    }

    @DeleteMapping("/regions/{id}")
    int deleteRegion(@Valid @PathVariable Integer id) {
        return regionDirectoryService.deleteRegionById(id);
    }
}
