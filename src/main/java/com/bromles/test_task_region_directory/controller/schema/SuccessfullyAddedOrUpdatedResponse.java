package com.bromles.test_task_region_directory.controller.schema;

import com.bromles.test_task_region_directory.entity.RegionDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Successfully added or updated", description = "Region successfully added or updated")
public class SuccessfullyAddedOrUpdatedResponse {

    @Schema(example = "true")
    private Boolean successful;

    @Schema(implementation = RegionDTO.class)
    private RegionDTO value;

    public Boolean getSuccessful() {
        return successful;
    }

    public RegionDTO getValue() {
        return value;
    }
}
