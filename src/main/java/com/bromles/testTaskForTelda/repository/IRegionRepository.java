package com.bromles.testTaskForTelda.repository;

import com.bromles.testTaskForTelda.entity.Region;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IRegionRepository {

    @Insert("INSERT INTO regions (id, name, short_name) VALUES (#{id}, #{name}, #{shortName})")
    @Options(useGeneratedKeys = true, keyProperty = "key", keyColumn = "key")
    int addRegion(Region region);

    @Select("SELECT key, id, name, short_name FROM regions")
    List<Region> getRegions();

    @Select("SELECT key, id, name, short_name FROM regions WHERE id = #{id}")
    Region getRegionById(String id);

    @Select("SELECT key, id, name, short_name FROM regions WHERE name = #{name}")
    List<Region> getRegionByName(String name);

    @Update("UPDATE regions set id = #{region.id}, name = #{region.name}, short_name = #{region.shortName} " +
            "WHERE id = #{id}")
    int updateRegionById(String id, Region region);

    @Delete("DELETE FROM regions WHERE id = #{id}")
    int deleteRegionById(String id);
}
