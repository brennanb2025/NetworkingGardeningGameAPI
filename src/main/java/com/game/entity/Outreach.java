package com.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Outreach {
    long id;
    long plantId;
    long userId;
    Date creationDatetime;
    String contents;
}


