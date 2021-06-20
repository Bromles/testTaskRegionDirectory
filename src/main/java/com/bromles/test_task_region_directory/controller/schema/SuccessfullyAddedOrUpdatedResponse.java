package com.bromles.test_task_region_directory.controller.schema;

import com.bromles.test_task_region_directory.entity.RegionDTO;

public class SuccessfullyAddedOrUpdatedResponse {

    private Boolean successful;

    private RegionDTO value;

    public Boolean getSuccessful() {
        return successful;
    }

    public RegionDTO getValue() {
        return value;
    }
}
