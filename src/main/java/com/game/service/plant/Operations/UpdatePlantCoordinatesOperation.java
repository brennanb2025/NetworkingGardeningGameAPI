package com.game.service.plant.Operations;

import com.game.dao.PlantDao;
import com.game.entity.Plant;
import com.game.entity.PlantGridData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePlantCoordinatesOperation {
    private final PlantDao plantDao;
    private final PlantCoordinateValidator coordinateValidator;

    public boolean execute(long id, int x, int y) {
        // Validate plant exists
        Plant plant = plantDao.getPlantById(id);
        if (plant == null) {
            throw new IllegalStateException("Plant not found: " + id);
        }

        // Validate coordinates
        PlantGridData plantGridData = new PlantGridData(id, x, y, plant.getPlantType(),
            coordinateValidator.getGridSizeForPlantType(plant.getPlantType()),
            coordinateValidator.getGridSizeForPlantType(plant.getPlantType()));
        
        if (!coordinateValidator.isValidPosition(plantGridData, plant.getUserId())) {
            throw new IllegalArgumentException("Invalid plant position: overlaps with existing plant");
        }

        // Update coordinates
        int updated = plantDao.updatePlantCoordinates(id, x, y);
        return updated > 0;
    }
} 