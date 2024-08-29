package com.game.dao;

import com.game.entity.Plant;
import com.game.mapper.PlantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PlantDao {

    @Autowired
    PlantMapper plantMapper;

    public List<Plant> getPlantsByUserId(long id) { return plantMapper.getPlantsByUserId(id); }

    public Plant getPlantById(long id) { return plantMapper.getPlantById(id); }

    public void addPlant(long id, int plantType, String name, String notes, int outreachDurationDays,
                         Date nextOutreachTime, short stage, int xCoord, int yCoord) {
        plantMapper.addPlant(id, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);
    }

    public void updatePlantInfo(long id, String name, String notes) {
        plantMapper.updatePlantInfo(id, name, notes);
    }

    public void updatePlantOutreachData(long id, int outreachDurationDays, Date nextOutreachTime) {
        plantMapper.updatePlantOutreachData(id, outreachDurationDays, nextOutreachTime);
    }

    public void updatePlantCoordinates(long id, int x, int y) {
        plantMapper.updatePlantCoordinates(id, x, y);
    }

    public void updatePlantTypeAndCoordinates(long id, int plantType, int x, int y) {
        plantMapper.updatePlantTypeAndCoordinates(id, plantType, x, y);
    }
}
