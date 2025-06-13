package com.game.service.outreach.CalculateNextOutreachTime;

import com.game.dao.OutreachDao;
import com.game.entity.SpecialOutreach;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OutreachTimeUpdater {
    private final OutreachDao outreachDao;
    private final OutreachTimeUtilities timeUtilities;
    
    public void updateNextOutreachTime(long plantId) {
        SpecialOutreach nextSpecialOutreach = outreachDao.getNextSpecialOutreachByPlantId(plantId);
        Long nextSpecialOutreachId = nextSpecialOutreach != null ? nextSpecialOutreach.getId() : null;
        
        LocalDate nextOutreachTime = timeUtilities.calculateNextOutreachTime(plantId, nextSpecialOutreachId);
        outreachDao.updatePlantNextOutreachTime(plantId, nextOutreachTime);
    }
} 