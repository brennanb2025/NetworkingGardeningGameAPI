package com.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpecialOutreach {
    long id;
    long plantId;
    long userId;
    LocalDate outreachTime;
    String notes;
    boolean completed;
}


