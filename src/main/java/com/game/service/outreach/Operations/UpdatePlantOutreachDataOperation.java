package com.game.service.outreach.Operations;

import com.game.dao.PlantDao;
import com.game.entity.Plant;
import com.game.service.outreach.CalculateNextOutreachTime.OutreachTimeUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePlantOutreachDataOperation {
    private final PlantDao plantDao;
    private final OutreachTimeUpdater timeUpdater;
    
    public boolean execute(long plantId, int outreachDurationDays) {
        // Validation
        if (outreachDurationDays <= 0) {
            throw new IllegalArgumentException("Outreach duration days must be positive");
        }
        
        Plant plant = plantDao.getPlantById(plantId);
        if (plant == null) {
            throw new IllegalStateException("Plant not found: " + plantId);
        }
        
        // Action
        int updated = plantDao.updatePlantOutreachData(plantId, outreachDurationDays, plant.getNextOutreachTime());
        
        // Update next outreach time using shared method
        timeUpdater.updateNextOutreachTime(plantId);
        
        return updated > 0;
    }
} 