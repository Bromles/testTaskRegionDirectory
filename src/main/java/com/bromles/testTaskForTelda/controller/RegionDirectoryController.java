package com.bromles.testTaskForTelda.controller;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.exception.DuplicateUniqueValuesException;
import com.bromles.testTaskForTelda.exception.handler.ExceptionResponseEntityGenerator;
import com.bromles.testTaskForTelda.service.IRegionDirectoryService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// TODO add input parameters validation
@Controller
@RequestMapping("v1/regions")
public class RegionDirectoryController {

    private final IRegionDirectoryService regionDirectoryService;

    private final String regionIdMapping = "/{id}";

    RegionDirectoryController(IRegionDirectoryService regionDirectoryService) {
        this.regionDirectoryService = regionDirectoryService;
    }

    @PostMapping
    ResponseEntity<Object> add(@Valid @RequestBody RegionDTO regionDTO) throws DuplicateUniqueValuesException {
        return ResponseEntity.ok(regionDirectoryService.add(regionDTO));
    }

    @GetMapping
    ResponseEntity<Object> getAll() {
        List<RegionDTO> regionDTOs = regionDirectoryService.getAll();

        if (regionDTOs.size() != 0) {

            return ResponseEntity.ok(regionDTOs);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NO_CONTENT, "message",
                    "There are no saved regions");
        }
    }

    @GetMapping(params = {"name"})
    ResponseEntity<Object> getByName(@Valid @RequestParam String name) {
        List<RegionDTO> regionDTOs = regionDirectoryService.getByName(name);

        if (regionDTOs.size() != 0) {

            return ResponseEntity.ok(regionDTOs);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND, "message",
                    "No regions found by name '" + name + "'");
        }
    }

    @GetMapping(params = {"short-name"})
    ResponseEntity<Object> getByShortName(@Valid @RequestParam("short-name") String shortName) {
        List<RegionDTO> regionDTOs = regionDirectoryService.getByShortName(shortName);

        if (regionDTOs.size() != 0) {

            return ResponseEntity.ok(regionDTOs);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND, "message",
                    "No regions found by short name '" + shortName + "'");
        }
    }

    @GetMapping(regionIdMapping)
    ResponseEntity<Object> getById(@Valid @PathVariable String id) {
        RegionDTO regionDTO = regionDirectoryService.getById(id);

        if (regionDTO != null) {
            return ResponseEntity.ok(regionDTO);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND, "message",
                    "No regions found by id '" + id + "'");
        }
    }

    @PutMapping(regionIdMapping)
    ResponseEntity<Object> updateById(@Valid @PathVariable String id, @Valid @RequestBody RegionDTO regionDTO) {
        int updatedRows = regionDirectoryService.updateById(id, regionDTO);

        if (updatedRows != 0) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("successful", true);
            response.put("value", regionDTO);

            return ResponseEntity.ok(response);
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND, "message",
                    "No regions updated by id '" + id + "'");
        }
    }

    @DeleteMapping(regionIdMapping)
    ResponseEntity<Object> deleteById(@Valid @PathVariable String id) {
        int deletedRows = regionDirectoryService.deleteById(id);

        if (deletedRows != 0) {
            return ResponseEntity.ok(new ImmutablePair<>("successful", true));
        }
        else {
            return ExceptionResponseEntityGenerator.generate(HttpStatus.NOT_FOUND, "message",
                    "No regions deleted by id '" + id + "'");
        }
    }

    @ExceptionHandler(DuplicateUniqueValuesException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateUniqueValuesException ex) {
        Map<String, Object> exceptionData = new LinkedHashMap<>();

        exceptionData.put("timestamp", new Date());
        exceptionData.put("status", HttpStatus.BAD_REQUEST.value());
        exceptionData.put("message", ex.getMessage());
        exceptionData.put("violated fields", ex.getViolatedFields());

        return ResponseEntity.badRequest().body(exceptionData);
    }
}
