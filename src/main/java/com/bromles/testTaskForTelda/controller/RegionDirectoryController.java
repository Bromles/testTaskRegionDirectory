package com.bromles.testTaskForTelda.controller;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.exception.ExceptionResponseEntityGenerator;
import com.bromles.testTaskForTelda.service.IRegionDirectoryService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    RegionDTO addRegion(@Valid @NotNull @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.addRegion(regionDTO);
    }

    @GetMapping
    ResponseEntity<Object> getRegion(@Valid @RequestParam(required = false) String name) {
        List<RegionDTO> regionDTOs;

        if (name != null) {
            regionDTOs = regionDirectoryService.getRegionByName(name);

            if(regionDTOs.size() != 0) {

                return new ResponseEntity<>(regionDTOs, HttpStatus.OK);
            }
            else {
                return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND,
                        new ImmutablePair<>("message", "No regions found by name '" + name + "'"));
            }
        }
        else {
            regionDTOs  = regionDirectoryService.getAll();

            if(regionDTOs.size() != 0) {

                return new ResponseEntity<>(regionDTOs, HttpStatus.OK);
            }
            else {
                return ExceptionResponseEntityGenerator.generate(HttpStatus.NO_CONTENT,
                        new ImmutablePair<>("message", "There are no saved regions"));
            }
        }
    }

    @GetMapping(idMapping)
    ResponseEntity<Object> getRegionById(@Valid @PathVariable String id) {
        RegionDTO regionDTO = regionDirectoryService.getRegionById(id);

        if(regionDTO != null) {
            return new ResponseEntity<>(regionDTO, HttpStatus.OK);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND,
                    new ImmutablePair<>("message", "No regions found by id '" + id + "'"));
        }
    }

    @PutMapping(idMapping)
    RegionDTO updateRegion(@Valid @PathVariable String id, @Valid @RequestBody RegionDTO regionDTO) {
        return regionDirectoryService.updateRegionById(id, regionDTO);
    }

    @DeleteMapping(idMapping)
    int deleteRegion(@Valid @PathVariable String id) {
        return regionDirectoryService.deleteRegionById(id);
    }
}
