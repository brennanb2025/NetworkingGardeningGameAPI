package com.game.service.outreach.CalculateNextOutreachTime;

import com.game.dao.OutreachDao;
import com.game.dao.PlantDao;
import com.game.entity.Outreach;
import com.game.entity.Plant;
import com.game.entity.SpecialOutreach;
import com.game.entity.SpecialOutreachCompletionRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DefaultOutreachTimeUtilities implements OutreachTimeUtilities {
    
    @Autowired
    private OutreachDao outreachDao;
    
    @Autowired
    private PlantDao plantDao;
    
    @Override
    public LocalDate calculateNextOutreachTime(long plantId, Long nextSpecialOutreachId) {
        // Get plant data
        Plant plant = plantDao.getPlantById(plantId);
        if (plant == null) {
            throw new IllegalStateException("Plant not found: " + plantId);
        }
        
        // Get the last outreach time (regular outreach)
        Outreach lastOutreach = outreachDao.getLastOutreach(plantId);
        LocalDate lastOutreachTime = lastOutreach != null ? lastOutreach.getCreationDatetime() : null;
        
        // Get the last special outreach completion time
        SpecialOutreachCompletionRecord lastSpecialOutreach = 
            outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId);
        LocalDate lastSpecialOutreachTime = lastSpecialOutreach != null ? 
            lastSpecialOutreach.getCreationDatetime() : null;
        
        // Find the latest outreach (either regular or special completion)
        LocalDate latestOutreach = getLatestOutreachTime(lastOutreachTime, lastSpecialOutreachTime);
        
        if (latestOutreach == null) {
            // No previous outreach, keep existing next outreach time
            return plant.getNextOutreachTime();
        }
        
        // Calculate next time based on latest outreach + plant's duration
        LocalDate calculatedNextTime = latestOutreach.plusDays(plant.getOutreachDurationDays());
        
        // Compare with next special outreach time if it exists
        if (nextSpecialOutreachId != null) {
            SpecialOutreach nextSpecialOutreach = outreachDao.getSpecialOutreachById(nextSpecialOutreachId);
            if (nextSpecialOutreach != null && 
                nextSpecialOutreach.getOutreachTime().isBefore(calculatedNextTime)) {
                return nextSpecialOutreach.getOutreachTime();
            }
        }
        
        return calculatedNextTime;
    }
    
    private LocalDate getLatestOutreachTime(LocalDate lastOutreachTime, LocalDate lastSpecialOutreachTime) {
        if (lastOutreachTime == null) {
            return lastSpecialOutreachTime; // Could be null
        }
        if (lastSpecialOutreachTime == null) {
            return lastOutreachTime; // Not null
        }
        // Both are not null, return the later one
        return lastOutreachTime.isAfter(lastSpecialOutreachTime) ? 
            lastOutreachTime : lastSpecialOutreachTime;
    }
} 