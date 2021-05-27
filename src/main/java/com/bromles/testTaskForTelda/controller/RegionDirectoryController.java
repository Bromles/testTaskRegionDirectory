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

    @PostMapping("/regions/add")
    RegionDTO addRegion(@Valid @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.addRegion(regionDTO);
    }

    @PostMapping("/regions/add-multi")
    List<RegionDTO> addListOfRegions(@RequestBody List<@Valid RegionDTO> regionDTOS) {
        return regionDirectoryService.addListOfRegions(regionDTOS);
    }

    @GetMapping("/regions/get")
    List<RegionDTO> getRegion(@Valid @RequestParam(required = false) String name) {
        if(name != null) {
            return regionDirectoryService.getRegionByName(name);
        } else {
            return regionDirectoryService.getAll();
        }
    }

    @GetMapping("/regions/get/{id}")
    RegionDTO getRegionById(@Valid @PathVariable String id) {
        return regionDirectoryService.getRegionById(id);
    }

    @PutMapping("/regions/update/{id}")
    RegionDTO updateRegion(@Valid @PathVariable String id, @Valid @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.updateRegionById(id, regionDTO);
    }

    @DeleteMapping("/regions/delete/{id}")
    int deleteRegion(@Valid @PathVariable String id) {
        return regionDirectoryService.deleteRegionById(id);
    }
}
