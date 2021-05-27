package com.bromles.testTaskForTelda.repository;

import com.bromles.testTaskForTelda.entity.Region;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IRegionRepository {

    @Insert("INSERT INTO regions(id, name, short_name) VALUES (#{id}, #{name}, #{shortName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addRegion(Region region);

    @Select("SELECT * FROM regions")
    List<Region> getRegions();

    @Select("SELECT * FROM regions WHERE id = #{id}")
    Region getRegionById(Integer id);

    @Update("UPDATE regions set name = #{region.name}, short_name = #{region.shortName} WHERE id = #{id}")
    int updateRegionById(Integer id, Region region);

    @Delete("DELETE FROM regions WHERE id = #{id}")
    int deleteRegionById(Integer id);
}
