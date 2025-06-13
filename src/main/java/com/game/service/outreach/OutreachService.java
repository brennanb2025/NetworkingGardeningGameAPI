package com.game.service.outreach;

import com.game.dao.OutreachDao;
import com.game.entity.Outreach;
import com.game.entity.SpecialOutreach;
import com.game.service.outreach.Operations.AddOutreachOperation;
import com.game.service.outreach.Operations.AddSpecialOutreachOperation;
import com.game.service.outreach.Operations.CompleteSpecialOutreachOperation;
import com.game.service.outreach.Operations.DeleteSpecialOutreachOperation;
import com.game.service.outreach.Operations.UpdatePlantOutreachDataOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/*
 * OutreachService delegates the operations to other handlers
 * 
 * The handlers are:
 * - AddOutreachOperation
 * - AddSpecialOutreachOperation
 * - DeleteSpecialOutreachOperation
 * - CompleteSpecialOutreachOperation
 * - UpdatePlantOutreachDataOperation
 * 
 * Each of these does the action and then recalculates the next outreach time
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class OutreachService {

    @Autowired
    OutreachDao outreachDao;

    @Autowired
    AddOutreachOperation addOutreachOperation;

    @Autowired
    AddSpecialOutreachOperation addSpecialOutreachOperation;

    @Autowired
    DeleteSpecialOutreachOperation deleteSpecialOutreachOperation;

    @Autowired
    CompleteSpecialOutreachOperation completeSpecialOutreachOperation;

    @Autowired
    UpdatePlantOutreachDataOperation updatePlantOutreachDataOperation;

    public void addOutreach(long userId, long plantId, String contents) {
        addOutreachOperation.execute(userId, plantId, contents);
    }

    public void addSpecialOutreach(long userId, long plantId, String notes, LocalDate outreachTime) {
        addSpecialOutreachOperation.execute(userId, plantId, notes, outreachTime);
    }

    public void deleteSpecialOutreachById(long id) {
        deleteSpecialOutreachOperation.execute(id);
    }

    public void addSpecialOutreachCompletionRecord(long specialOutreachId, String notes) {
        completeSpecialOutreachOperation.execute(specialOutreachId, notes);
    }

    public List<Outreach> getOutreachesByPlantId(long plantId) {
        return outreachDao.getOutreachesByPlantId(plantId);
    }

    public List<SpecialOutreach> getSpecialOutreachesByPlantId(long plantId) {
        return outreachDao.getSpecialOutreachesByPlantId(plantId);
    }

    public boolean updatePlantOutreachData(long id, int outreachDurationDays) {
        return updatePlantOutreachDataOperation.execute(id, outreachDurationDays);
    }
}