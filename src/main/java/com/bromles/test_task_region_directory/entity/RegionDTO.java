package com.bromles.test_task_region_directory.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class RegionDTO {

    @NotBlank(message = "Region code cannot be blank")
    @Pattern(regexp = ("([0-9]{2}[1-9])|([0-9][1-9][0-9])|([1-9][0-9]{2})|([0-9][1-9])|([1-9][0-9])"),
            message = "Region id must be 2 or 3 digits and mustn't contain only zeros")
    public String id;

    @NotBlank(message = "Region name cannot be blank")
    @Pattern(regexp = "[а-яА-Я() -]+", message = "Region name must contain only Cyrillic, spaces, dashes and brackets")
    public String name;

    @Pattern(regexp = "[А-Я]{3}", message = "Region short name must be 3 capital Cyrillic letters")
    public String shortName;

    public RegionDTO(String id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    public RegionDTO() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RegionDTO regionDTO = (RegionDTO) obj;
        return id.equals(regionDTO.id) && name.equals(regionDTO.name) && shortName.equals(regionDTO.shortName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shortName);
    }
}
