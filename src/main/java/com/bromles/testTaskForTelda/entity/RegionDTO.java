package com.bromles.testTaskForTelda.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegionDTO {

    @NotBlank(message = "Region code cannot be blank")
    @Pattern(regexp = "\\d{2,3}", message = "Region code must be 2 or 3 digits")
    public String id;

    @NotBlank(message = "Region name cannot be blank")
    @Pattern(regexp = "[а-яА-Я() -]+", message = "Region name can contain only Cyrillic, spaces, dashes and brackets")
    public String name;

    @Size(min = 3, max = 3, message = "Region short name must be 3 characters")
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
