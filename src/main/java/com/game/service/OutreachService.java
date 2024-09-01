package com.game.service;


import com.game.dao.OutreachDao;
import com.game.dao.PlantDao;
import com.game.dao.UserDao;
import com.game.entity.Outreach;
import com.game.entity.Plant;
import com.game.entity.SpecialOutreach;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class OutreachService {

    @Autowired
    OutreachDao outreachDao;

    @Autowired
    UserDao userDao;

    @Autowired
    PlantDao plantDao;

    private void updateOutreachTime(long plantId, LocalDate nextOutreachTime) {
        outreachDao.updatePlantNextOutreachTime(
                plantId,
                nextOutreachTime);
    }

    public void addOutreach(long userId, long plantId, String contents) {
        outreachDao.addOutreach(userId, plantId, contents);
        Plant plant = plantDao.getPlantById(plantId);
        SpecialOutreach nextSpecialOutreach = outreachDao.getNextSpecialOutreachByPlantId(plantId);
        if(nextSpecialOutreach.getOutreachTime().isBefore(  // next special outreach time is before:
                LocalDate.now().plusDays(                   // the current date
                        plant.getOutreachDurationDays()     // plus the # days before the next set outreach
                )
        )) {
            updateOutreachTime(plantId, nextSpecialOutreach.getOutreachTime());
        } else {
            updateOutreachTime(plantId, LocalDate.now().plusDays(       // the current date
                    plant.getOutreachDurationDays()                     // plus the # days before the next set outreach
            ));
        }

        // TODO: set next outreach time to earliest (min) between:
        //  - current time + duration
        //  - next special outreach time
    }

    public void addSpecialOutreach(long userId, long plantId, String notes, LocalDate outreachTime) {
        outreachDao.addSpecialOutreach(userId, plantId, notes, outreachTime);
        Plant plant = plantDao.getPlantById(plantId);

        if(outreachTime.isBefore(plant.getNextOutreachTime())) {
            updateOutreachTime(plantId, outreachTime);
        } else {
            updateOutreachTime(plantId, plant.getNextOutreachTime());
        }
        // set next outreach time to earliest (min) between:
        //  - currently set next outreach time
        //      (will be the soonest time, either the next default one or the next special time)
        //  - this new next special outreach time
    }

    public void deleteSpecialOutreachById(long id) {
        // set next outreach time to earliest (min) between:
        //  - last outreach time (either last special outreach or last regular outreach) + duration
        //  - next special outreach time

        SpecialOutreach specialOutreach = outreachDao.getSpecialOutreachById(id);
        outreachDao.removeSpecialOutreachById(id);

        long plantId = specialOutreach.getPlantId();

        // need plant for getting duration in last outreach time + duration
        Plant plant = plantDao.getPlantById(plantId);

        // next special outreach time
        LocalDate nextSpecialOutreachTime = outreachDao.getNextSpecialOutreachByPlantId(
            plantId
        ).getOutreachTime();

        // last outreach time
        LocalDate lastOutreachTime = outreachDao.getLastOutreach(plantId).getCreationDatetime();

        // compare next special outreach time w/ last outreach time + duration between outreaches
        // (what it would be if next special outreach time was after it)
        if(nextSpecialOutreachTime.isBefore(
            lastOutreachTime.plusDays(plant.getOutreachDurationDays())
        )) {
            updateOutreachTime(plantId, nextSpecialOutreachTime);
        } else {
            updateOutreachTime(
                plantId,
                lastOutreachTime.plusDays(plant.getOutreachDurationDays())
            );
        }
    }

    public void addSpecialOutreachCompletionRecord(long specialOutreachId, String notes) {
        SpecialOutreach specialOutreach = outreachDao.getSpecialOutreachById(specialOutreachId);
        if(specialOutreach == null) {
            // throw error
        }
        outreachDao.addSpecialOutreachCompletionRecord(specialOutreachId, notes);
        outreachDao.setSpecialOutreachAsCompleted(specialOutreachId);

        SpecialOutreach nextSpecialOutreach = outreachDao.getNextSpecialOutreachByPlantId(
                specialOutreach.getPlantId());

        Plant plant = plantDao.getPlantById(specialOutreach.getPlantId());

        // set next outreach time to earliest (min) between:
        //  - current time + duration
        //  - next special outreach time
        if(nextSpecialOutreach.getOutreachTime().isBefore(      // next special outreach time is before
                LocalDate.now().plusDays(                       // current time +
                        plant.getOutreachDurationDays()         // # days between outreaches
                )
        )) {
            updateOutreachTime(                                 // set to special outreach time
                    specialOutreach.getPlantId(),
                    nextSpecialOutreach.getOutreachTime());
        } else {
            updateOutreachTime(specialOutreach.getPlantId(),
                    LocalDate.now().plusDays(                   // set to current time + duration days
                            plant.getOutreachDurationDays()
            ));
        }

        // TODO: check potential null reference exception here
    }

    public List<Outreach> getOutreachesByPlantId(long plantId) {
        return outreachDao.getOutreachesByPlantId(plantId);
    }

    public List<SpecialOutreach> getSpecialOutreachesByPlantId(long plantId) {
        return outreachDao.getSpecialOutreachesByPlantId(plantId);
    }
}