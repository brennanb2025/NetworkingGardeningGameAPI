package com.game.service.outreach.Operations;

import com.game.dao.OutreachDao;
import com.game.entity.SpecialOutreach;
import com.game.service.outreach.CalculateNextOutreachTime.OutreachTimeUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompleteSpecialOutreachOperation {
    private final OutreachDao outreachDao;
    private final OutreachTimeUpdater timeUpdater;
    
    public void execute(long specialOutreachId, String notes) {
        // Validation
        if (notes == null || notes.trim().isEmpty()) {
            throw new IllegalArgumentException("Notes cannot be empty");
        }
        
        SpecialOutreach outreach = outreachDao.getSpecialOutreachById(specialOutreachId);
        if (outreach == null) {
            throw new IllegalStateException("Special outreach not found: " + specialOutreachId);
        }
        
        // Action
        outreachDao.addSpecialOutreachCompletionRecord(specialOutreachId, notes);
        outreachDao.setSpecialOutreachAsCompleted(specialOutreachId);
        
        // Update next outreach time using shared method
        timeUpdater.updateNextOutreachTime(outreach.getPlantId());
    }
} 