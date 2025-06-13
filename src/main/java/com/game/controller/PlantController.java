package com.game.controller;

import com.game.entity.Plant;
import com.game.service.plant.PlantService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RestController
public class PlantController {

    @Autowired
    PlantService plantService;

    @PostMapping("/addplant")
    public long addPlant(@RequestParam("userId") long userid,
                         @RequestParam("plantType") int plantType,
                         @RequestParam("name") String name,
                         @RequestParam("notes") String notes,
                         @RequestParam("outreachDurationDays") int outreachDurationDays,
                         @RequestParam("nextOutreachTime") @DateTimeFormat(pattern="MM-dd-yyyy") LocalDate nextOutreachTime,
                         @RequestParam("stage") short stage,
                         @RequestParam("xCoord") int xCoord,
                         @RequestParam("yCoord") int yCoord) {
        return plantService.addPlant(userid, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);
    }

    @GetMapping("/getplantsbyuserid")
    public List<Plant> getPlantsByUserId(@RequestParam("userid") long userid) {
        return plantService.getPlantsByUserId(userid);
    }

    @GetMapping("/getplantbyid")
    public Plant getPlantById(@RequestParam("id") long id) {
        return plantService.getPlantById(id);
    }

    @PutMapping("/updateplantinfo")
    public boolean updatePlantInfo(@RequestParam("id") long id,
                                @RequestParam("name") String name,
                                @RequestParam("notes") String notes) {
        return plantService.updatePlantInfo(id, name, notes);
    }

    @PutMapping("/updateplantcoordinates")
    public boolean updatePlantCoordinates(
            @RequestParam("id") long id,
            @RequestParam("x") int x,
            @RequestParam("y") int y) {
        return plantService.updatePlantCoordinates(id, x, y);
    }

    @PutMapping("/updateplanttypeandcoordinates")
    public boolean updatePlantTypeAndCoordinates(
            @RequestParam("id") long id,
            @RequestParam("plantType") int plantType,
            @RequestParam("x") int x,
            @RequestParam("y") int y) {
        return plantService.updatePlantTypeAndCoordinates(id, plantType, x, y);
    }
}
