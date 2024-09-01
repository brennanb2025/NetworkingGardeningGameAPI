package com.game.dao;

import com.game.entity.Plant;
import com.game.mapper.PlantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PlantDao {

    @Autowired
    PlantMapper plantMapper;

    public List<Plant> getPlantsByUserId(long id) { return plantMapper.getPlantsByUserId(id); }

    public Plant getPlantById(long id) { return plantMapper.getPlantById(id); }

    public void addPlant(long userid, int plantType, String name, String notes, int outreachDurationDays,
                         LocalDate nextOutreachTime, short stage, int xCoord, int yCoord) {
        plantMapper.addPlant(userid, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);
    }

    public int updatePlantInfo(long id, String name, String notes) {
        return plantMapper.updatePlantInfo(id, name, notes);
    }

    public int updatePlantOutreachData(long id, int outreachDurationDays, LocalDate nextOutreachTime) {
        return plantMapper.updatePlantOutreachData(id, outreachDurationDays, nextOutreachTime);
    }

    public int updatePlantCoordinates(long id, int x, int y) {
        return plantMapper.updatePlantCoordinates(id, x, y);
    }

    public int updatePlantTypeAndCoordinates(long id, int plantType, int x, int y) {
        return plantMapper.updatePlantTypeAndCoordinates(id, plantType, x, y);
    }
}
