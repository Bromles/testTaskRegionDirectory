package com.bromles.testTaskForTelda.controller;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.exception.ExceptionResponseEntityGenerator;
import com.bromles.testTaskForTelda.service.IRegionDirectoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/regions")
public class RegionDirectoryController {

    private final IRegionDirectoryService regionDirectoryService;

    private final String idMapping = "/{id}";

    RegionDirectoryController(IRegionDirectoryService regionDirectoryService) {
        this.regionDirectoryService = regionDirectoryService;
    }

    @PostMapping
    ResponseEntity<Object> addRegion(@Valid @RequestBody RegionDTO regionDTO) {
        RegionDTO result = regionDirectoryService.addRegion(regionDTO);

        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.BAD_REQUEST, "message",
                    "Duplicate id with value '" + regionDTO.id + "'");
        }
    }

    @GetMapping
    ResponseEntity<Object> getRegion() {
        List<RegionDTO> regionDTOs = regionDirectoryService.getAll();

        if (regionDTOs.size() != 0) {

            return new ResponseEntity<>(regionDTOs, HttpStatus.OK);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NO_CONTENT, "message",
                    "There are no saved regions");
        }
    }

    @GetMapping(params = {"name"})
    ResponseEntity<Object> getRegionByName(@Valid @RequestParam String name) {
        List<RegionDTO> regionDTOs = regionDirectoryService.getRegionByName(name);

        if (regionDTOs.size() != 0) {

            return new ResponseEntity<>(regionDTOs, HttpStatus.OK);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND, "message",
                    "No regions found by name '" + name + "'");
        }
    }

    @GetMapping(idMapping)
    ResponseEntity<Object> getRegionById(@Valid @PathVariable String id) {
        RegionDTO regionDTO = regionDirectoryService.getRegionById(id);

        if (regionDTO != null) {
            return new ResponseEntity<>(regionDTO, HttpStatus.OK);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND, "message",
                    "No regions found by id '" + id + "'");
        }
    }

    // TODO add protection
    @PutMapping(idMapping)
    RegionDTO updateRegion(@Valid @PathVariable String id, @Valid @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.updateRegionById(id, regionDTO);
    }

    // TODO add protection
    @DeleteMapping(idMapping)
    int deleteRegion(@Valid @PathVariable String id) {
        return regionDirectoryService.deleteRegionById(id);
    }
}
