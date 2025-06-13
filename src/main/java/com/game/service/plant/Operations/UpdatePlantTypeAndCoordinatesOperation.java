package com.game.service.plant.Operations;

import com.game.dao.PlantDao;
import com.game.entity.Plant;
import com.game.entity.PlantGridData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePlantTypeAndCoordinatesOperation {
    private final PlantDao plantDao;
    private final PlantCoordinateValidator coordinateValidator;

    public boolean execute(long id, int plantType, int x, int y) {
        // Validate plant exists
        Plant plant = plantDao.getPlantById(id);
        if (plant == null) {
            throw new IllegalStateException("Plant not found: " + id);
        }

        // Validate coordinates with new plant type
        PlantGridData plantGridData = new PlantGridData(id, x, y, plantType,
            coordinateValidator.getGridSizeForPlantType(plantType),
            coordinateValidator.getGridSizeForPlantType(plantType));
        
        if (!coordinateValidator.isValidPosition(plantGridData, plant.getUserId())) {
            throw new IllegalArgumentException("Invalid plant position: overlaps with existing plant");
        }

        // Update plant type and coordinates
        int updated = plantDao.updatePlantTypeAndCoordinates(id, plantType, x, y);
        return updated > 0;
    }
} 