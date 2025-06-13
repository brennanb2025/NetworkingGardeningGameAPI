package com.game.service.outreach.CalculateNextOutreachTime;

import java.time.LocalDate;

public interface OutreachTimeUtilities {
    LocalDate calculateNextOutreachTime(long plantId, Long nextSpecialOutreachId);
} 