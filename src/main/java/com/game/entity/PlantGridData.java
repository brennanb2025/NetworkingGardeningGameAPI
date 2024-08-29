package com.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlantGridData {
    long id;
    int x;
    int y;
    int plantType;
    int width;
    int height;
}
