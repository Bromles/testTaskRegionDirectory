package com.bromles.testTaskForTelda.controller;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.service.IRegionDirectoryService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1")
public class RegionDirectoryController {

    private final IRegionDirectoryService regionDirectoryService;

    RegionDirectoryController(IRegionDirectoryService regionDirectoryService) {
        this.regionDirectoryService = regionDirectoryService;
    }

    @PostMapping("/regions")
    RegionDTO addRegion(@Valid @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.addRegion(regionDTO);
    }

    @GetMapping("/regions")
    List<RegionDTO> getAll() {
        return regionDirectoryService.getAll();
    }

    @GetMapping("/regions/{id}")
    RegionDTO getRegion(@Valid @PathVariable String id) {
        return regionDirectoryService.getRegionById(id);
    }

    @PutMapping("/regions/{id}")
    RegionDTO updateRegion(@Valid @PathVariable String id, @Valid @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.updateRegionById(id, regionDTO);
    }

    @DeleteMapping("/regions/{id}")
    int deleteRegion(@Valid @PathVariable String id) {
        return regionDirectoryService.deleteRegionById(id);
    }
}
