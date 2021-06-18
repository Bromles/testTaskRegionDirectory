package com.bromles.test_task_region_directory.repository;

import com.bromles.test_task_region_directory.entity.Region;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Интерфейс репозитория справочника регионов
 */
@Mapper
public interface IRegionRepository {

    /**
     * Добавляет регион в репозиторий
     *
     * @param region Тип: {@link Region}. Сущность для добавления
     */
    @Insert("INSERT INTO regions (id, name, short_name) VALUES (#{id}, #{name}, #{shortName})")
    @Options(useGeneratedKeys = true, keyProperty = "key", keyColumn = "key")
    void save(Region region);

    /**
     * Получает список регионов
     *
     * @return Возвращает список регионов
     */
    @Select("SELECT key, id, name, short_name FROM regions ORDER BY name")
    List<Region> getAll();

    /**
     * Получает регион по идентификатору
     *
     * @param id Тип: {@link String}. Идентификатор региона, по которому осуществляется поиск
     * @return Возвращает регион
     */
    @Select("SELECT key, id, name, short_name FROM regions WHERE id = #{id}")
    Region getById(String id);

    /**
     * Получает список регионов по наименованию
     *
     * @param name Тип: {@link String}. Наименование региона, по которому осуществляется поиск
     * @return Возвращает список регионов
     */
    @Select("SELECT key, id, name, short_name FROM regions WHERE name = #{name} ORDER BY name")
    List<Region> getByName(String name);

    /**
     * Получает список регионов по началу наименования
     *
     * @param nameBeginning Тип: {@link String}. Начало наименования региона, по которому осуществляется поиск
     * @return Возвращает список регионов
     */
    @Select("SELECT key, id, name, short_name FROM regions WHERE name LIKE '${nameBeginning}%' ORDER BY name")
    List<Region> getByNameBeginning(String nameBeginning);

    /**
     * Получает список регионов по сокращенному наименованию
     *
     * @param shortName Тип: {@link String}. Сокращенное наименование региона, по которому осуществляется поиск
     * @return Возвращает список регионов
     */
    @Select("SELECT key, id, name, short_name FROM regions WHERE short_name = #{shortName} ORDER BY name")
    List<Region> getByShortName(String shortName);

    /**
     * Обновляет регион по идентификатору
     *
     * @param id Тип: {@link String}. Идентификатор, по которому осуществляется поиск региона в репозитории для обновления
     * @param region Тип: {@link Region}. Сущность, заменяющая данные
     * @return Возвращает количество обновленных записей
     */
    @Update("UPDATE regions set id = #{region.id}, name = #{region.name}, short_name = #{region.shortName} " +
            "WHERE id = #{id}")
    int updateById(String id, Region region);

    /**
     * Удаляет регион по идентификатору
     *
     * @param id Тип: {@link String}. Идентификатор, по которому осуществляется поиск региона для удаления
     * @return Возвращает количество удаленных записей
     */
    @Delete("DELETE FROM regions WHERE id = #{id}")
    int deleteById(String id);
}
