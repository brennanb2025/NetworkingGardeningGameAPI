package com.game.mapper;


import com.game.entity.Outreach;
import com.game.entity.SpecialOutreach;
import com.game.entity.SpecialOutreachCompletionRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OutreachMapper {

    @Insert("insert into outreach (userId, plantId, contents) " +
            "values (#{userId}, #{plantId}, #{contents})")
    void addOutreach(@Param("userId") long userId,
                     @Param("plantId") long plantId,
                     @Param("contents") String contents);

    @Select("SELECT * FROM outreach WHERE plantId = #{plantId} " +
            "ORDER BY creationDatetime DESC " +
            "LIMIT 1") // TODO: top 1?
    Outreach getLastOutreach(@Param("plantId") long plantId);

    @Select("SELECT socr.* " +
            "FROM special_outreach_completion_records socr " +
            "JOIN special_outreach so ON socr.specialOutreachId = so.id " +
            "ORDER BY socr.creationDatetime DESC " +
            "LIMIT 1") // TODO: top 1?
    SpecialOutreachCompletionRecord getLastSpecialOutreachCompletionRecordByPlantId(@Param("plantId") long plantId);

    @Update("update plants set nextOutreachTime=#{nextOutreachTime} where id=#{id}")
    void updatePlantNextOutreachTime(@Param("id") long id,
                                     @Param("nextOutreachTime") LocalDate nextOutreachTime);

    @Insert("INSERT INTO special_outreach (userId, plantId, notes, outreachTime, completed) " +
            "values (#{userId}, #{plantId}, #{notes}, #{outreachTime}, false)")
    void addSpecialOutreach(@Param("userId") long userId,
                            @Param("plantId") long plantId,
                            @Param("notes") String notes,
                            @Param("outreachTime") LocalDate outreachTime);

    // TODO: fix this
    @Delete("DELETE FROM special_outreach where id=#{id}")
    void deleteSpecialOutreachById(@Param("id") long id);

    @Select("SELECT * FROM special_outreach WHERE id = #{id}")
    SpecialOutreach getSpecialOutreachById(@Param("id") long id);

    @Select("SELECT * FROM special_outreach WHERE plantId = #{plantId} " +
            "AND completed = false AND outreachTime > CURRENT_DATE " +
            "ORDER BY outreachTime ASC " +
            "LIMIT 1") // TODO: top 1?
    SpecialOutreach getNextSpecialOutreachByPlantId(@Param("plantId") long plantId);


    @Insert("insert into special_outreach_completion_records (specialOutreachId, notes) " +
            "values (#{specialOutreachId}, #{notes})")
    void addSpecialOutreachCompletionRecord(@Param("specialOutreachId") long specialOutreachId,
                                            @Param("notes") String notes);

    @Update("update special_outreach set completed=true where id=#{id}")
    void setSpecialOutreachAsCompleted(@Param("id") long id);

    // DESC = latest outreach first
    @Select("SELECT * FROM outreach WHERE plantId = #{plantId} ORDER BY creationDatetime DESC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "plantId", column = "plantId"),
            @Result(property = "creationDatetime", column = "creationDatetime"),
            @Result(property = "contents", column = "contents")
    })
    List<Outreach> getOutreachesByPlantId(@Param("plantId") long plantId);

    // ASC = earliest next special outreach first
    @Select("SELECT * FROM special_outreach WHERE plantId = #{plantId} ORDER BY outreachTime ASC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "userId"),
            @Result(property = "plantId", column = "plantId"),
            @Result(property = "creationDatetime", column = "creationDatetime"),
            @Result(property = "contents", column = "contents"),
            @Result(property = "completed", column = "completed")
    })
    List<SpecialOutreach> getSpecialOutreachesByPlantId(@Param("plantId") long plantId);
}