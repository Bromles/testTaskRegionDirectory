package com.bromles.testTaskForTelda.controller;

import com.bromles.testTaskForTelda.entity.RegionDTO;
import com.bromles.testTaskForTelda.exception.DuplicateUniqueValuesException;
import com.bromles.testTaskForTelda.exception.RecordNotFoundException;
import com.bromles.testTaskForTelda.service.IRegionDirectoryService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "v1/regions", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class RegionDirectoryController {

    private final IRegionDirectoryService regionDirectoryService;

    private final String regionIdMapping = "/{id}";

    RegionDirectoryController(IRegionDirectoryService regionDirectoryService) {
        this.regionDirectoryService = regionDirectoryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> add(@Valid @RequestBody RegionDTO regionDTO) throws DuplicateUniqueValuesException {
        regionDirectoryService.add(regionDTO);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("successful", true);
        response.put("value", regionDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() throws RecordNotFoundException {
        List<RegionDTO> regionDTOs = regionDirectoryService.getAll();

        return ResponseEntity.ok(regionDTOs);
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<Object> getByName(
            @NotBlank(message = "Region name can't be blank")
            @Pattern(regexp = "[а-яА-Я() -]+",
                    message = "Region name must contain only Cyrillic, spaces, dashes and brackets")
            @RequestParam String name) throws RecordNotFoundException {
        List<RegionDTO> regionDTOs = regionDirectoryService.getByName(name);

        return ResponseEntity.ok(regionDTOs);
    }

    @GetMapping(params = {"name-beginning"})
    public ResponseEntity<Object> getByNameBeginning(
            @Pattern(regexp = "[А-Я][а-я]*",
                    message = "Beginning of region name can't be blank, must contain only Cyrillic letters and " +
                            "begins with Capital one")
            @RequestParam("name-beginning") String nameBeginning) throws RecordNotFoundException {
        List<RegionDTO> regionDTOs = regionDirectoryService.getByNameBeginning(nameBeginning);

        return ResponseEntity.ok(regionDTOs);
    }

    @GetMapping(params = {"short-name"})
    public ResponseEntity<Object> getByShortName(
            @Pattern(regexp = "[А-Я]{3}",
                    message = "Region short name can't be blank and must be 3 Capital Cyrillic letters")
            @RequestParam("short-name") String shortName) throws RecordNotFoundException {
        List<RegionDTO> regionDTOs = regionDirectoryService.getByShortName(shortName);

        return ResponseEntity.ok(regionDTOs);
    }

    @GetMapping(regionIdMapping)
    public ResponseEntity<Object> getById(
            @Pattern(regexp = ("([0-9]{2}[1-9])|([0-9][1-9][0-9])|([1-9][0-9]{2})|([0-9][1-9])|([1-9][0-9])"),
                    message = "Region id must be 2 or 3 digits and mustn't contain only zeros")
            @PathVariable String id) throws RecordNotFoundException {
        RegionDTO regionDTO = regionDirectoryService.getById(id);

        return ResponseEntity.ok(regionDTO);
    }

    @PutMapping(value = regionIdMapping, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateById(
            @Pattern(regexp = ("([0-9]{2}[1-9])|([0-9][1-9][0-9])|([1-9][0-9]{2})|([0-9][1-9])|([1-9][0-9])"),
                    message = "Region id must be 2 or 3 digits and mustn't contain only zeros")
            @PathVariable String id, @Valid @RequestBody RegionDTO regionDTO) throws RecordNotFoundException, DuplicateUniqueValuesException {
        regionDirectoryService.updateById(id, regionDTO);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("successful", true);
        response.put("value", regionDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(regionIdMapping)
    public ResponseEntity<Object> deleteById(
            @Pattern(regexp = ("([0-9]{2}[1-9])|([0-9][1-9][0-9])|([1-9][0-9]{2})|([0-9][1-9])|([1-9][0-9])"),
                    message = "Region id must be 2 or 3 digits and mustn't contain only zeros")
            @PathVariable String id) throws RecordNotFoundException {
        regionDirectoryService.deleteById(id);

        return ResponseEntity.ok(new ImmutablePair<>("successful", true));
    }

    @ExceptionHandler(DuplicateUniqueValuesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateUniqueValuesException ex) {
        Map<String, Object> exceptionData = new LinkedHashMap<>();

        exceptionData.put("timestamp", new Date());
        exceptionData.put("status", HttpStatus.BAD_REQUEST.value());
        exceptionData.put("message", ex.getMessage());
        exceptionData.put("violated-fields", ex.getViolatedFields());

        return ResponseEntity.badRequest().body(exceptionData);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex) {
        Map<String, Object> exceptionData = new LinkedHashMap<>();

        exceptionData.put("timestamp", new Date());
        exceptionData.put("status", HttpStatus.NOT_FOUND.value());
        exceptionData.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionData);
    }
}
