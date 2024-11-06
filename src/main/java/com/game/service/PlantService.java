package com.game.service;

import com.game.dao.OutreachDao;
import com.game.dao.PlantDao;
import com.game.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class PlantService {

    @Autowired
    PlantDao plantDao;

    @Autowired
    OutreachDao outreachDao;

    public List<Plant> getPlantsByUserId(long id){
        return plantDao.getPlantsByUserId(id);
    }

    public Plant getPlantById(long id) {
        return plantDao.getPlantById(id);
    }

    public long addPlant(long userid, int plantType, String name, String notes, int outreachDurationDays,
                         LocalDate nextOutreachTime, short stage, int xCoord, int yCoord) {
        Plant plant = new Plant(
                0,
                userid,
                plantType,
                name,
                notes,
                outreachDurationDays,
                nextOutreachTime,
                stage,
                xCoord,
                yCoord);
        return plantDao.addPlant(plant);
    }

    public boolean updatePlantInfo(long id, String name, String notes) {
        return plantDao.updatePlantInfo(id, name, notes) == 1;
    }

    public boolean updatePlantOutreachData(long id, int outreachDurationDays) {
        // TODO: set next outreach time to earliest (min) between:
        //  - last outreach time (either last special outreach or last regular outreach) + this new duration
        //      - note: last special outreach time will be stored in the creationDatetime of a
        //          specialOutreachCompletionRecord.
        //  - next special outreach time

        Plant thisPlant = plantDao.getPlantById(id);
        if(thisPlant == null) {
            return false;
        }

        // next special outreach time
        SpecialOutreach nextSpecialOutreach = outreachDao.getNextSpecialOutreachByPlantId(
                id
        );
        LocalDate nextSpecialOutreachTime = nextSpecialOutreach == null ? null : nextSpecialOutreach.getOutreachTime();


        // last outreach time
        Outreach lastOutreach = outreachDao.getLastOutreach(id);
        LocalDate lastOutreachTime = lastOutreach == null ? null : lastOutreach.getCreationDatetime();

        // last special outreach completed time
        SpecialOutreachCompletionRecord lastSpecialOutreach = outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(
                id
        );
        LocalDate lastSpecialOutreachTime = lastSpecialOutreach == null ? null : lastSpecialOutreach.getCreationDatetime();

        // latestOutreach = latest outreach between last outreach and last special outreach
        LocalDate latestOutreach;
        if(lastOutreachTime == null) {
            latestOutreach = lastSpecialOutreachTime; // could be null also
        }
        else if(lastSpecialOutreachTime == null) {
            latestOutreach = lastOutreachTime; // not null
        }
        else { // neither are null
            latestOutreach = lastOutreachTime.isAfter(lastSpecialOutreachTime) ?
                    lastOutreachTime :
                    lastSpecialOutreachTime;
        }

        if(latestOutreach == null) {
            // if latest outreach == null and next special outreach time != null, the next outreach time
            // will be already set to the next special outreach time.
            // So we can update to getNextOutreachTime regardless of whether nextSpecialOutreachTime is null.
            return plantDao.updatePlantOutreachData(
                    id,
                    outreachDurationDays,
                    thisPlant.getNextOutreachTime()
                    // both are null: leave it unchanged
                    // (user initially set it as that)
            ) == 1;
        } else {
            // latestOutreach != null
            if(nextSpecialOutreachTime == null) {
                return plantDao.updatePlantOutreachData(
                        id,
                        outreachDurationDays,
                        latestOutreach.plusDays(outreachDurationDays) // set it to last time + days between outreaches
                ) == 1;
            } else { // neither are null
                // compare next special outreach time w/ last outreach time + new duration
                if(nextSpecialOutreachTime.isBefore(                        // next special time before last time
                        latestOutreach.plusDays(outreachDurationDays)       // + duration days
                )) {
                    return plantDao.updatePlantOutreachData(id, outreachDurationDays, nextSpecialOutreachTime) == 1;
                    // set it to next time
                } else {
                    return plantDao.updatePlantOutreachData(
                            id,
                            outreachDurationDays,
                            latestOutreach.plusDays(outreachDurationDays) // set it to last time + days between outreaches
                    ) == 1;
                }
            }
        }
    }

    public boolean updatePlantCoordinates(long id, int x, int y) {

        // run algorithm to check if coordinates are valid
        // (not overlapping with any other plants)
        // this is a double-check for the frontend.

        Plant thisPlant = plantDao.getPlantById(id);


        PlantGridData thisPlantGridData = new PlantGridData(
                id,
                x,
                y,
                thisPlant.getPlantType(),
                3, // should be from config w/ thisPlant.getPlantType()
                3
        );

        if(checkValidPositionDataForPlant(thisPlantGridData, thisPlant.getUserId())) {
            return plantDao.updatePlantCoordinates(id, x, y) == 1;
        }
        return false;
    }

    // also update coords
    public boolean updatePlantTypeAndCoordinates(long id, int plantType, int x, int y) {

        // run algorithm to check if new plant type is valid
        // (not overlapping with any other plants)
        // also, in this algorithm, perform the check with the new plantType.
        // this is a double-check for the frontend.

        Plant thisPlant = plantDao.getPlantById(id);

        PlantGridData thisPlantGridData = new PlantGridData(
                id,
                x,
                y,
                plantType,
                3, // should be from config w/ plantType
                3
        );

        if(checkValidPositionDataForPlant(thisPlantGridData, thisPlant.getUserId())) {
            return plantDao.updatePlantTypeAndCoordinates(id, plantType, x, y) == 1;
        }
        return false;
    }

    private boolean checkValidPositionDataForPlant(
            PlantGridData plantGridData,
            long userId) {

        // algorithm to check if new plant type is valid
        // (not overlapping with any other plants)

        // use userId to get all of the user's other plants
        // ignore the plant with the same id as plantGridData.id

        List<Plant> otherPlants = getPlantsByUserId(userId);
        List<PlantGridData> otherPlantGridData = new ArrayList<PlantGridData>();
        for(Plant p : otherPlants) {
            otherPlantGridData.add(new PlantGridData(
                    p.getId(),
                    p.getXCoord(),
                    p.getYCoord(),
                    p.getPlantType(),
                    3,
                    3 // TODO: add config singleton
            ));
        }

        return true;
    }
}