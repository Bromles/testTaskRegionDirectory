package com.bromles.testTaskForTelda.repository;

import com.bromles.testTaskForTelda.entity.Region;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IRegionRepository {

    @Insert("INSERT INTO regions (id, name, short_name) VALUES (#{id}, #{name}, #{shortName})")
    @Options(useGeneratedKeys = true, keyProperty = "key", keyColumn = "key")
    void save(Region region);

    @Select("SELECT key, id, name, short_name FROM regions")
    List<Region> getAll();

    @Select("SELECT key, id, name, short_name FROM regions WHERE id = #{id}")
    Region getById(String id);

    @Select("SELECT key, id, name, short_name FROM regions WHERE name = #{name}")
    List<Region> getByName(String name);

    @Select("SELECT key, id, name, short_name FROM regions WHERE short_name = #{shortName}")
    List<Region> getByShortName(String shortName);

    @Update("UPDATE regions set id = #{region.id}, name = #{region.name}, short_name = #{region.shortName} " +
            "WHERE id = #{id}")
    int updateById(String id, Region region);

    @Delete("DELETE FROM regions WHERE id = #{id}")
    int deleteById(String id);
}
