package com.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpecialOutreachCompletionRecord {
    long id;
    long specialOutreachId;
    LocalDate creationDatetime;
    String notes;
}