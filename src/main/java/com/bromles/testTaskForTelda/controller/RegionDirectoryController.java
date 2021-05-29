package com.bromles.testTaskForTelda.controller;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.service.IRegionDirectoryService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("v1/regions")
public class RegionDirectoryController {

    private final IRegionDirectoryService regionDirectoryService;

    RegionDirectoryController(IRegionDirectoryService regionDirectoryService) {
        this.regionDirectoryService = regionDirectoryService;
    }

    @PostMapping
    RegionDTO addRegion(@Valid @NotNull @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.addRegion(regionDTO);
    }

    @GetMapping
    List<RegionDTO> getRegion(@Valid @RequestParam(required = false) String name) {
        if (name != null) {
            return regionDirectoryService.getRegionByName(name);
        }
        else {
            return regionDirectoryService.getAll();
        }
    }

    @GetMapping("/{id}")
    RegionDTO getRegionById(@Valid @PathVariable String id) {
        return regionDirectoryService.getRegionById(id);
    }

    @PutMapping("/{id}")
    RegionDTO updateRegion(@Valid @PathVariable String id, @Valid @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.updateRegionById(id, regionDTO);
    }

    @DeleteMapping("/{id}")
    int deleteRegion(@Valid @PathVariable String id) {
        return regionDirectoryService.deleteRegionById(id);
    }
}
