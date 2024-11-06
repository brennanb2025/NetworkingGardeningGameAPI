package com.game.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Plant {
    public Plant(long id, long userId, int plantType, String name,
                 String notes, int outreachDurationDays, LocalDate nextOutreachTime,
                 short stage, int xCoord, int yCoord) {
        this.id = id;
        this.userId = userId;
        this.plantType = plantType;
        this.name = name;
        this.notes = notes;
        this.outreachDurationDays = outreachDurationDays;
        this.nextOutreachTime = nextOutreachTime;
        this.stage = stage;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }
    long id;
    long userId;
    int plantType;
    String name;
    String notes;
    int outreachDurationDays;
    LocalDate nextOutreachTime;
    short stage; // stage of growth the plant is in (1, 2, 3)
    @JsonProperty("xCoord")
    int xCoord;
    @JsonProperty("yCoord")
    int yCoord;
    LocalDate creationDatetime;
}
