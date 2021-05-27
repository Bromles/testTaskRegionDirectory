package com.bromles.testTaskForTelda.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RegionDTO {

    @NotBlank(message = "Region code cannot be blank")
    @Pattern(regexp = "[0-9]{3}|[0-9]{2}", message = "Region code must be 2 or 3 digits")
    public String id;

    @NotBlank(message = "Region name cannot be blank")
    public String name;

    @Length(min = 3, max = 3, message = "Region short name must be 3 characters")
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
