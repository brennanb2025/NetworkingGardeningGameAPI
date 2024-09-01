package com.game.dao;

import com.game.entity.Outreach;
import com.game.entity.SpecialOutreach;
import com.game.entity.SpecialOutreachCompletionRecord;
import com.game.mapper.OutreachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class OutreachDao {

    @Autowired
    OutreachMapper outreachMapper;

    public void updatePlantNextOutreachTime(long id, LocalDate nextOutreachTime) {
        outreachMapper.updatePlantNextOutreachTime(id, nextOutreachTime);
    }

    public void addOutreach(long userId, long plantId, String contents) {
        outreachMapper.addOutreach(userId, plantId, contents);
    }

    public Outreach getLastOutreach(long plantId) {
        return outreachMapper.getLastOutreach(plantId);
    }

    public void addSpecialOutreach(long userId, long plantId, String notes, LocalDate outreachTime) {
        outreachMapper.addSpecialOutreach(userId, plantId, notes, outreachTime);
    }

    public void removeSpecialOutreachById(long id) {
        outreachMapper.deleteSpecialOutreachById(id);
    }

    public void addSpecialOutreachCompletionRecord(long specialOutreachId, String notes) {
        outreachMapper.addSpecialOutreachCompletionRecord(specialOutreachId, notes);
    }

    public SpecialOutreach getSpecialOutreachById(long id) {
        return outreachMapper.getSpecialOutreachById(id);
    }

    public SpecialOutreach getNextSpecialOutreachByPlantId(long plantId) {
        return outreachMapper.getNextSpecialOutreachByPlantId(plantId);
    }

    public SpecialOutreachCompletionRecord getLastSpecialOutreachCompletionRecordByPlantId(long plantId) {
        return outreachMapper.getLastSpecialOutreachCompletionRecordByPlantId(plantId);
    }

    public void setSpecialOutreachAsCompleted(long id) {
        outreachMapper.setSpecialOutreachAsCompleted(id);
    }

    public List<Outreach> getOutreachesByPlantId(long plantId) {
        return outreachMapper.getOutreachesByPlantId(plantId);
    }

    public List<SpecialOutreach> getSpecialOutreachesByPlantId(long plantId) {
        return outreachMapper.getSpecialOutreachesByPlantId(plantId);
    }
}