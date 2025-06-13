package com.game.service.plant.Operations;

import com.game.dao.PlantDao;
import com.game.entity.Plant;
import com.game.entity.PlantGridData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AddPlantOperation {
    private final PlantDao plantDao;
    private final PlantCoordinateValidator coordinateValidator;

    public long execute(long userId, int plantType, String name, String notes, 
                       int outreachDurationDays, LocalDate nextOutreachTime, 
                       short stage, int xCoord, int yCoord) {
        // Validate coordinates
        PlantGridData plantGridData = new PlantGridData(0L, xCoord, yCoord, plantType,
            coordinateValidator.getGridSizeForPlantType(plantType), 
            coordinateValidator.getGridSizeForPlantType(plantType));
        
        if (!coordinateValidator.isValidPosition(plantGridData, userId)) {
            throw new IllegalArgumentException("Invalid plant position: overlaps with existing plant");
        }

        // Create and save plant
        Plant plant = new Plant();
        plant.setName(name);
        plant.setPlantType(plantType);
        plant.setXCoord(xCoord);
        plant.setYCoord(yCoord);
        plant.setUserId(userId);
        plant.setNotes(notes);
        plant.setOutreachDurationDays(outreachDurationDays);
        plant.setNextOutreachTime(nextOutreachTime);
        plant.setStage(stage);
        
        return plantDao.addPlant(plant);
    }
} 