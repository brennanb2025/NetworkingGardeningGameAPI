package com.game.service.outreach.Operations;

import com.game.dao.OutreachDao;
import com.game.entity.SpecialOutreach;
import com.game.service.outreach.CalculateNextOutreachTime.OutreachTimeUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteSpecialOutreachOperation {
    private final OutreachDao outreachDao;
    private final OutreachTimeUpdater timeUpdater;
    
    public void execute(long specialOutreachId) {
        // Validation
        SpecialOutreach outreach = outreachDao.getSpecialOutreachById(specialOutreachId);
        if (outreach == null) {
            throw new IllegalStateException("Special outreach not found: " + specialOutreachId);
        }
        
        // Action
        outreachDao.removeSpecialOutreachById(specialOutreachId);
        
        // Update next outreach time using shared method
        timeUpdater.updateNextOutreachTime(outreach.getPlantId());
    }
} 