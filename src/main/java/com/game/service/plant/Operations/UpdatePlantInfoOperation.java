package com.game.service.plant.Operations;

import com.game.dao.PlantDao;
import com.game.entity.Plant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePlantInfoOperation {
    private final PlantDao plantDao;

    public boolean execute(long id, String name, String notes) {
        // Validate plant exists
        Plant plant = plantDao.getPlantById(id);
        if (plant == null) {
            throw new IllegalStateException("Plant not found: " + id);
        }

        // Update plant info
        int updated = plantDao.updatePlantInfo(id, name, notes);
        return updated > 0;
    }
} 