package com.bromles.test_task_region_directory.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Объект трансфера данных для сущности региона
 */
@Schema(name = "Region", description = "Region")
public class RegionDTO {

    /**
     * Идентификатор региона
     * <p>
     * Состоит из 2 или 3 цифр, не может содержать только нули.
     */
    @Schema(name = "id", description = "Code of region, must be 2 or 3 digits and mustn't contain only zeros",
            example = "35")
    @NotBlank(message = "Region code cannot be blank")
    @Pattern(regexp = ("([0-9]{2}[1-9])|([0-9][1-9][0-9])|([1-9][0-9]{2})|([0-9][1-9])|([1-9][0-9])"),
            message = "Region id must be 2 or 3 digits and mustn't contain only zeros")
    public String id;

    /**
     * Наименование региона
     * <p>
     * Может состоять только из кириллических букв, пробелов, дефисов и скобок. Не может быть пустым
     */
    @Schema(name = "name", description = "Name of region, must contain only Cyrillic, spaces, dashes and brackets",
            example = "Вологодская область")
    @NotBlank(message = "Region name cannot be blank")
    @Pattern(regexp = "[а-яА-Я() -]+", message = "Region name must contain only Cyrillic, spaces, dashes and brackets")
    public String name;

    /**
     * Сокращенное наименование региона
     * <p>
     * Состоит из 3 заглавных кириллических букв
     */
    @Schema(name = "short name", description = "Short name of region, must be 3 capital Cyrillic letters", example =
            "ВОЛ")
    @Pattern(regexp = "[А-Я]{3}", message = "Region short name must be 3 capital Cyrillic letters")
    public String shortName;

    /**
     * Конструктор объекта трансфера данных со всеми параметрами
     *
     * @param id        Тип: {@link String}. Идентификатор региона
     * @param name      Тип: {@link String}. Наименование региона
     * @param shortName Тип: {@link String}. Сокращенное наименование региона
     */
    public RegionDTO(String id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    /**
     * Конструктор объекта трансфера данных без параметров
     */
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
