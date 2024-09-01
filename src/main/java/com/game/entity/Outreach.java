package com.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Outreach {
    long id;
    long plantId;
    long userId;
    LocalDate creationDatetime;
    String contents;
}


