package com.game.controller;

import com.game.entity.Plant;
import com.game.service.PlantService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RestController
public class PlantController {

    @Autowired
    PlantService plantService;

    @PostMapping("/addplant")
    public void addPlant(@RequestParam("userId") long userid,
                         @RequestParam("plantType") int plantType,
                         @RequestParam("name") String name,
                         @RequestParam("notes") String notes,
                         @RequestParam("outreachDurationDays") int outreachDurationDays,
                         @RequestParam("nextOutreachTime") Date nextOutreachTime,
                         @RequestParam("stage") short stage,
                         @RequestParam("xCoord") int xCoord,
                         @RequestParam("yCoord") int yCoord) {
        plantService.addPlant(userid, plantType, name, notes, outreachDurationDays,
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
    public void updatePlantInfo(@RequestParam("id") long id,
                                @RequestParam("name") String name,
                                @RequestParam("notes") String notes) {
        plantService.updatePlantInfo(id, name, notes);
    }

    @PutMapping("/updateplantoutreachdurationdays")
    public void updatePlantOutreachDurationDays(
            @RequestParam("id") long id,
            @RequestParam("outreachDurationDays") int outreachDurationDays) {
        plantService.updatePlantOutreachData(id, outreachDurationDays);
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
