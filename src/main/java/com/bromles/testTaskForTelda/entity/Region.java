package com.bromles.testTaskForTelda.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class Region {

    @Positive
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    // TODO add pattern validation
    private String shortName;

    public Region(Integer id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
