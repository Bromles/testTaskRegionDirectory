package com.bromles.test_task_region_directory.entity;

import java.util.Objects;

public class Region {

    private Integer key;

    private String id;

    private String name;

    private String shortName;

    public Region(Integer key, String id, String name, String shortName) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    public Region(String id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    public Region(RegionDTO regionDTO) {
        this.id = regionDTO.id;
        this.name = regionDTO.name;
        this.shortName = regionDTO.shortName;
    }

    public RegionDTO toDTO() {
        return new RegionDTO(id, name, shortName);
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Region region = (Region) obj;
        return key.equals(region.key) && id.equals(region.id) && name.equals(region.name) && shortName.equals(region.shortName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, id, name, shortName);
    }
}
