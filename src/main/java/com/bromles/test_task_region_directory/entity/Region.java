package com.bromles.test_task_region_directory.entity;

import java.util.Objects;

/**
 * Сущность региона
 */
public class Region {

    /**
     * Первичный ключ для хранения в репозитории записи о регионе
     */
    private Integer key;

    /**
     * Идентификатор региона
     */
    private String id;

    /**
     * Наименование региона
     */
    private String name;

    /**
     * Сокращенное наименование региона
     */
    private String shortName;

    /**
     * Конструктор сущности региона со всеми параметрами
     *
     * @param key Тип: Integer. Первичный ключ записи региона
     * @param id Тип: String. Идентификатор региона
     * @param name Тип: String. Наименование региона
     * @param shortName Тип: String. Сокращенное наименование региона
     */
    public Region(Integer key, String id, String name, String shortName) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    /**
     * Конструктор сущности региона со всеми параметрами, кроме первичного ключа
     *
     * @param id Тип: String. Идентификатор региона
     * @param name Тип: String. Наименование региона
     * @param shortName Тип: String. Сокращенное наименование региона
     */
    public Region(String id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }

    /**
     * Конструктор сущности региона по объекту трансфера данных
     *
     * @param regionDTO Тип: regionDTO. Объект трансфера данных, на основе которого создается сущность региона
     */
    public Region(RegionDTO regionDTO) {
        this.id = regionDTO.id;
        this.name = regionDTO.name;
        this.shortName = regionDTO.shortName;
    }

    /**
     * Генерирует объект трансфера данных из текущей сущности
     *
     * @return Возвращает сгенерированный объект трансфера данных
     */
    public RegionDTO toDTO() {
        return new RegionDTO(id, name, shortName);
    }

    /**
     * Получает первичный ключ сущности
     *
     * @return Возвращает первичный ключ записи региона
     * @see #setKey(Integer)
     */
    public Integer getKey() {
        return key;
    }

    /**
     * Сохраняет значение первичного ключа в сущности
     *
     * @param key Тип: Integer. Первичный ключ записи региона
     * @see #getKey()
     */
    public void setKey(Integer key) {
        this.key = key;
    }

    /**
     * Получает идентификатор сущности
     *
     * @return Возвращает идентификатор региона
     * @see #setId(String)
     */
    public String getId() {
        return id;
    }

    /**
     * Сохраняет значение идентификатора в сущности
     *
     * @param id Тип: String. Идентификатор региона
     * @see #getId()
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Получает значение наименования сущности
     *
     * @return Возвращает значение наименования региона
     * @see #setName(String)
     */
    public String getName() {
        return name;
    }

    /**
     * Сохраняет значение наименования в сущности
     *
     * @param name Тип: String. Наименование региона
     * @see #getName()
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получает значение сокращенного наименования сущности
     *
     * @return Возвращает значение сокращенного наименования региона
     * @see #setShortName(String)
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Сохраняет значение сокращенного наименования в сущности
     *
     * @param shortName Тип: String. Сокращенное наименование региона
     * @see #getShortName()
     */
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
