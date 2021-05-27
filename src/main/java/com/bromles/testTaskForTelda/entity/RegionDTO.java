package com.bromles.testTaskForTelda.entity;


import javax.validation.constraints.NotBlank;

public class RegionDTO {

    @NotBlank
    // TODO add pattern validation
    public String id;

    @NotBlank
    public String name;

    @NotBlank
    public String shortName;

    public RegionDTO(String id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    public RegionDTO(Region region) {
        this.id = region.getId();
        this.name = region.getName();
        this.shortName = region.getShortName();
    }
}
