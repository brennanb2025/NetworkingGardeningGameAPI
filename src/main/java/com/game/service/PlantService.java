package com.game.service;

import com.game.dao.OutreachDao;
import com.game.dao.PlantDao;
import com.game.dao.UserDao;
import com.game.entity.Plant;
import com.game.entity.PlantGridData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addPlant(long id, int plantType, String name, String notes, int outreachDurationDays,
                         Date nextOutreachTime, short stage, int xCoord, int yCoord) {
        plantDao.addPlant(id, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);
    }

    public void updatePlantInfo(long id, String name, String notes) {
        plantDao.updatePlantInfo(id, name, notes);
    }

    public void updatePlantOutreachData(long id, int outreachDurationDays) {
        // TODO: set next outreach time to earliest (min) between:
        //  - last outreach time (either last special outreach or last regular outreach) + this new duration
        //  - next special outreach time

        // next special outreach time
        Date nextSpecialOutreachTime = outreachDao.getNextSpecialOutreachByPlantId(
                id
        ).getOutreachTime();

        // last outreach time
        Date lastOutreachTime = outreachDao.getLastOutreach(id).getCreationDatetime();

        // compare next special outreach time w/ last outreach time + new duration
        if(nextSpecialOutreachTime.before(
                lastOutreachTime// + outreachDurationDays
        )) {
            plantDao.updatePlantOutreachData(id, outreachDurationDays, nextSpecialOutreachTime);
        } else {
            plantDao.updatePlantOutreachData(
                    id,
                    outreachDurationDays,
                    lastOutreachTime// + outreachDurationDays
            );
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
            plantDao.updatePlantCoordinates(id, x, y);
            return true;
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
            plantDao.updatePlantTypeAndCoordinates(id, plantType, x, y);
            return true;
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
        return true;
    }
}