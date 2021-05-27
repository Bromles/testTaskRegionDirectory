package com.bromles.testTaskForTelda.entity;

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

}
