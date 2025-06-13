package com.game.service.outreach.Operations;

import com.game.dao.OutreachDao;
import com.game.service.outreach.CalculateNextOutreachTime.OutreachTimeUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AddSpecialOutreachOperation {
    private final OutreachDao outreachDao;
    private final OutreachTimeUpdater timeUpdater;
    
    public void execute(long userId, long plantId, String notes, LocalDate outreachTime) {
        // Validation
        if (notes == null || notes.trim().isEmpty()) {
            throw new IllegalArgumentException("Notes cannot be empty");
        }
        if (outreachTime == null) {
            throw new IllegalArgumentException("Outreach time cannot be null");
        }
        
        // Action
        outreachDao.addSpecialOutreach(userId, plantId, notes, outreachTime);
        
        // Update next outreach time using shared method
        timeUpdater.updateNextOutreachTime(plantId);
    }
} 