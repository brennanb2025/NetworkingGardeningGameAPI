package com.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpecialOutreach {
    long id;
    long plantId;
    long userId;
    Date outreachTime;
    String notes;
    boolean completed;
}


