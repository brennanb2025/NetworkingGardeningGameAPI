package com.game.service.plant;

import com.game.dao.PlantDao;
import com.game.entity.*;
import com.game.service.plant.Operations.AddPlantOperation;
import com.game.service.plant.Operations.UpdatePlantCoordinatesOperation;
import com.game.service.plant.Operations.UpdatePlantInfoOperation;
import com.game.service.plant.Operations.UpdatePlantTypeAndCoordinatesOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlantService {

    @Autowired
    private PlantDao plantDao;

    @Autowired
    private AddPlantOperation addPlantOperation;

    @Autowired
    private UpdatePlantInfoOperation updatePlantInfoOperation;

    @Autowired
    private UpdatePlantCoordinatesOperation updatePlantCoordinatesOperation;

    @Autowired
    private UpdatePlantTypeAndCoordinatesOperation updatePlantTypeAndCoordinatesOperation;

    public List<Plant> getPlantsByUserId(long id) {
        return plantDao.getPlantsByUserId(id);
    }

    public Plant getPlantById(long id) {
        return plantDao.getPlantById(id);
    }

    public long addPlant(long userId, int plantType, String name, String notes, 
                        int outreachDurationDays, LocalDate nextOutreachTime, 
                        short stage, int xCoord, int yCoord) {
        return addPlantOperation.execute(userId, plantType, name, notes, 
            outreachDurationDays, nextOutreachTime, stage, xCoord, yCoord);
    }

    public boolean updatePlantInfo(long id, String name, String notes) {
        return updatePlantInfoOperation.execute(id, name, notes);
    }

    public boolean updatePlantCoordinates(long id, int x, int y) {
        return updatePlantCoordinatesOperation.execute(id, x, y);
    }

    public boolean updatePlantTypeAndCoordinates(long id, int plantType, int x, int y) {
        return updatePlantTypeAndCoordinatesOperation.execute(id, plantType, x, y);
    }
}