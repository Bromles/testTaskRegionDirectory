package com.bromles.testTaskForTelda.controller.schema;

import com.bromles.testTaskForTelda.entity.RegionDTO;
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
