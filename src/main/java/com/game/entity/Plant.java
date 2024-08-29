package com.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Plant {
    long id;
    long userId;
    int plantType;
    String name;
    String notes;
    int outreachDurationDays;
    Date nextOutreachTime;
    short stage; // stage of growth the plant is in (1, 2, 3)
    int xCoord;
    int yCoord;
    Date creationDatetime;
}
