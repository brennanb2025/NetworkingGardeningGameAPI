package com.game.service.plant.Operations;

import com.game.dao.PlantDao;
import com.game.entity.Plant;
import com.game.entity.PlantGridData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
@RequiredArgsConstructor
public class PlantCoordinateValidator {
    private final PlantDao plantDao;

    public boolean isValidPosition(PlantGridData plantGridData, long userId) {
        // Get all plants for the user
       List<Plant> otherPlants = plantDao.getPlantsByUserId(userId);

       // Convert to grid data
       List<PlantGridData> otherPlantGridData = otherPlants.stream()
           .filter(p -> p.getId() != plantGridData.getId()) // Exclude the current plant
           .map(p -> new PlantGridData(
               p.getId(),
               p.getXCoord(),
               p.getYCoord(),
               p.getPlantType(),
               getGridSizeForPlantType(p.getPlantType()),
               getGridSizeForPlantType(p.getPlantType())
           ))
           .toList();

       // Check for overlaps
       return otherPlantGridData.stream()
           .noneMatch(other -> isOverlapping(plantGridData, other));
    }

    public int getGridSizeForPlantType(int plantType) {
        // TODO: Move to configuration
        return 3;
    }

    private boolean isOverlapping(PlantGridData plant1, PlantGridData plant2) {
      return false;
//        return Math.abs(plant1.getXCoord() - plant2.getXCoord()) < plant1.getGridSize() &&
//               Math.abs(plant1.getYCoord() - plant2.getYCoord()) < plant1.getGridSize();
    }
} 