package com.bromles.test_task_region_directory.controller.schema;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Successfully deleted", description = "Region deleted successfully")
public class SuccessfullyDeletedResponse {

    @Schema(example = "true")
    private Boolean successful;

    public Boolean getSuccessful() {
        return successful;
    }

}
