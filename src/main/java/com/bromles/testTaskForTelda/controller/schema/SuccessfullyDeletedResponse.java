package com.bromles.testTaskForTelda.controller.schema;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Successfully deleted", description = "Region deleted successfully")
public class SuccessfullyDeletedResponse {

    @Schema(example = "true")
    private Boolean successful;

    public Boolean getSuccessful() {
        return successful;
    }

}
