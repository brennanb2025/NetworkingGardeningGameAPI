package com.game.mapper;


import com.game.entity.Plant;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface PlantMapper {

    @Insert("insert into plants (userId, plantType, name, notes, outreachDurationDays, " +
            "nextOutreachTime, stage, xCoord, yCoord) " +
            "values (#{userId}, #{plantType}, #{name}, #{notes}, #{outreachDurationDays}, " +
            "#{nextOutreachTime}, #{stage}, #{xCoord}, #{yCoord})")
    void addPlant(long userId, int plantType, String name, String notes, int outreachDurationDays,
                  Date nextOutreachTime, short stage, int xCoord, int yCoord);

    // DESC = earliest outreach first
    @Select("SELECT * FROM plants WHERE userId = #{userId} ORDER BY nextOutreachTime ASC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "plantType", column = "plantType"),
            @Result(property = "name", column = "name"),
            @Result(property = "notes", column = "notes"),
            @Result(property = "outreachDurationDays", column = "outreachDurationDays"),
            @Result(property = "nextOutreachTime", column = "nextOutreachTime"),
            @Result(property = "stage", column = "stage"),
            @Result(property = "xCoord", column = "xCoord"),
            @Result(property = "yCoord", column = "yCoord"),
            @Result(property = "creationDatetime", column = "creationDatetime")
    })
    List<Plant> getPlantsByUserId(@Param("userId") long userId);

    @Select("SELECT * FROM plants WHERE id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "plantType", column = "plantType"),
            @Result(property = "name", column = "name"),
            @Result(property = "notes", column = "notes"),
            @Result(property = "outreachDurationDays", column = "outreachDurationDays"),
            @Result(property = "nextOutreachTime", column = "nextOutreachTime"),
            @Result(property = "stage", column = "stage"),
            @Result(property = "xCoord", column = "xCoord"),
            @Result(property = "yCoord", column = "yCoord"),
            @Result(property = "creationDatetime", column = "creationDatetime")
    })
    Plant getPlantById(@Param("id") long id);

    @Update("UPDATE plants set name=#{name}, notes=#{notes} where id=#{id}")
    void updatePlantInfo(@Param("id") long id,
                         @Param("name") String name,
                         @Param("notes") String notes);

    // these will always be updated together
    @Update("UPDATE plants set outreachDurationDays=#{outreachDurationDays}, " +
            "nextOutreachTime=#{nextOutreachTime} where id=#{id}")
    void updatePlantOutreachData(@Param("id") long id,
                         @Param("outreachDurationDays") int outreachDurationDays,
                         @Param("nextOutreachTime") Date nextOutreachTime);

    // separate for coords - check if valid
    @Update("UPDATE plants set xCoord=#{x}, yCoord=#{y} where id=#{id}")
    void updatePlantCoordinates(@Param("id") long id, @Param("x") int x, @Param("y") int y);

    // separate for plant type - check if valid
    @Update("UPDATE plants set plantType=#{plantType}, " +
            "xCoord=#{x}, yCoord=#{y} " +
            "where id=#{id}")
    void updatePlantTypeAndCoordinates(@Param("id") long id,
                                       @Param("plantType") int plantType,
                                       @Param("x") int x,
                                       @Param("y") int y);
}