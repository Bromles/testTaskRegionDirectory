package com.bromles.test_task_region_directory.controller;

import com.bromles.test_task_region_directory.controller.schema.SuccessfullyAddedOrUpdatedResponse;
import com.bromles.test_task_region_directory.controller.schema.SuccessfullyDeletedResponse;
import com.bromles.test_task_region_directory.entity.RegionDTO;
import com.bromles.test_task_region_directory.exception.DuplicateUniqueValuesException;
import com.bromles.test_task_region_directory.exception.RecordNotFoundException;
import com.bromles.test_task_region_directory.service.IRegionDirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Region directory", description = "Directory of regions")
@RestController
@RequestMapping(value = "v1/regions", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class RegionDirectoryController {
    private final IRegionDirectoryService regionDirectoryService;

    private final String regionIdMapping = "/{id}";

    RegionDirectoryController(IRegionDirectoryService regionDirectoryService) {
        this.regionDirectoryService = regionDirectoryService;
    }

    @Operation(summary = "Add a region")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Region added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessfullyAddedOrUpdatedResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Region supplied", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> add(
            @Parameter(description = "Region to add", required = true,
                    schema = @Schema(implementation = RegionDTO.class))
            @Valid @RequestBody RegionDTO regionDTO) throws DuplicateUniqueValuesException {
        regionDirectoryService.add(regionDTO);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("successful", true);
        response.put("value", regionDTO);

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RegionDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No regions found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Object> getAll() throws RecordNotFoundException {
        List<RegionDTO> regionDTOs = regionDirectoryService.getAll();

        return ResponseEntity.ok(regionDTOs);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RegionDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid name of region supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "No regions found", content = @Content)
    })
    @GetMapping(params = {"name"})
    public ResponseEntity<Object> getByName(
            @Parameter(description = "Name of required region")
            @NotBlank(message = "Region name can't be blank")
            @Size(max = 255, message = "Region name can't be longer than 255 characters")
            @Pattern(regexp = "[а-яА-Я() -]+",
                    message = "Region name must contain only Cyrillic, spaces, dashes and brackets")
            @RequestParam String name) throws RecordNotFoundException {
        List<RegionDTO> regionDTOs = regionDirectoryService.getByName(name);

        return ResponseEntity.ok(regionDTOs);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RegionDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid beginning of region name supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No regions found", content = @Content)
    })
    @GetMapping(params = {"name-beginning"})
    public ResponseEntity<Object> getByNameBeginning(
            @Parameter(description = "Beginning of name of required region")
            @Size(max = 255, message = "Beginning of region name can't be longer than 255 characters")
            @Pattern(regexp = "[А-Я][а-я]*",
                    message = "Beginning of region name can't be blank, must contain only Cyrillic letters and " +
                            "begins with Capital one")
            @RequestParam("name-beginning") String nameBeginning) throws RecordNotFoundException {
        List<RegionDTO> regionDTOs = regionDirectoryService.getByNameBeginning(nameBeginning);

        return ResponseEntity.ok(regionDTOs);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RegionDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid short name of region supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "No regions found", content = @Content)
    })
    @GetMapping(params = {"short-name"})
    public ResponseEntity<Object> getByShortName(
            @Parameter(description = "Short name of required region")
            @Pattern(regexp = "[А-Я]{3}",
                    message = "Region short name can't be blank and must be 3 Capital Cyrillic letters")
            @RequestParam("short-name") String shortName) throws RecordNotFoundException {
        List<RegionDTO> regionDTOs = regionDirectoryService.getByShortName(shortName);

        return ResponseEntity.ok(regionDTOs);
    }

    @Operation(summary = "Get a region by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid code of region supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "No regions found", content = @Content)
    })
    @GetMapping(regionIdMapping)
    public ResponseEntity<Object> getById(
            @Parameter(description = "Code of required region", required = true)
            @Pattern(regexp = ("([0-9]{2}[1-9])|([0-9][1-9][0-9])|([1-9][0-9]{2})|([0-9][1-9])|([1-9][0-9])"),
                    message = "Region id must be 2 or 3 digits and mustn't contain only zeros")
            @PathVariable String id) throws RecordNotFoundException {
        RegionDTO regionDTO = regionDirectoryService.getById(id);

        return ResponseEntity.ok(regionDTO);
    }

    @Operation(summary = "Update an existing region by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessfullyAddedOrUpdatedResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid region or code of region supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "No regions found", content = @Content)
    })
    @PutMapping(value = regionIdMapping, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateById(
            @Parameter(description = "Code of required region", required = true)
            @Pattern(regexp = ("([0-9]{2}[1-9])|([0-9][1-9][0-9])|([1-9][0-9]{2})|([0-9][1-9])|([1-9][0-9])"),
                    message = "Region id must be 2 or 3 digits and mustn't contain only zeros")
            @PathVariable String id,

            @Parameter(description = "New region data", required = true,
                    schema = @Schema(implementation = RegionDTO.class))
            @Valid @RequestBody RegionDTO regionDTO) throws RecordNotFoundException, DuplicateUniqueValuesException {
        regionDirectoryService.updateById(id, regionDTO);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("successful", true);
        response.put("value", regionDTO);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an existing region by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = SuccessfullyDeletedResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid code of region supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "No regions found", content = @Content)
    })
    @DeleteMapping(regionIdMapping)
    public ResponseEntity<Object> deleteById(
            @Parameter(description = "Code of required region", required = true,
                    schema = @Schema(implementation = String.class))
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
