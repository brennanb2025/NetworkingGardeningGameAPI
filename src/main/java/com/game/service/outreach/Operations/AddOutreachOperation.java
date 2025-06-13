package com.game.service.outreach.Operations;

import com.game.dao.OutreachDao;
import com.game.service.outreach.CalculateNextOutreachTime.OutreachTimeUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddOutreachOperation {
    private final OutreachDao outreachDao;
    private final OutreachTimeUpdater timeUpdater;
    
    public void execute(long userId, long plantId, String contents) {
        // Validation
        if (contents == null || contents.trim().isEmpty()) {
            throw new IllegalArgumentException("Contents cannot be empty");
        }
        
        // Action
        outreachDao.addOutreach(userId, plantId, contents);
        
        // Update next outreach time using shared method
        timeUpdater.updateNextOutreachTime(plantId);
    }
} 