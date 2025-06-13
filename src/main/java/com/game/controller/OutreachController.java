package com.game.controller;

import com.game.entity.Outreach;
import com.game.entity.SpecialOutreach;
import com.game.service.outreach.OutreachService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RestController
public class OutreachController {

    @Autowired
    OutreachService outreachService;

    @PostMapping("/addoutreach")
    public void addOutreach(@RequestParam("userId") long userId,
                            @RequestParam("plantId") int plantId,
                            @RequestParam("contents") String contents) {
        outreachService.addOutreach(userId, plantId, contents);
    }

    @PostMapping("/addspecialoutreach")
    public void addSpecialOutreach(@RequestParam("userId") long userId,
                                   @RequestParam("plantId") long plantId,
                                   @RequestParam("notes") String notes,
                                   @RequestParam("outreachTime") LocalDate outreachTime) {
        outreachService.addSpecialOutreach(userId, plantId, notes, outreachTime);
    }

    @PostMapping("/deletespecialoutreach")
    public void deleteSpecialOutreach(@RequestParam("id") long id) {
        outreachService.deleteSpecialOutreachById(id);
    }

    @PostMapping("/completespecialoutreach")
    public void completeSpecialOutreach(
            @RequestParam("specialOutreachId") long specialOutreachId,
            @RequestParam("notes") String notes) {
        outreachService.addSpecialOutreachCompletionRecord(specialOutreachId, notes);
    }

    @GetMapping("/getoutreachesbyplantid")
    public List<Outreach> getOutreachesByPlantId(@RequestParam("plantId") long plantId) {
        return outreachService.getOutreachesByPlantId(plantId);
    }

    @GetMapping("/getspecialoutreachesbyplantid")
    public List<SpecialOutreach> getSpecialOutreachesByPlantId(@RequestParam("plantId") long plantId) {
        return outreachService.getSpecialOutreachesByPlantId(plantId);
    }

    @PutMapping("/updateplantoutreachdurationdays")
    public boolean updatePlantOutreachDurationDays(
            @RequestParam("id") long id,
            @RequestParam("outreachDurationDays") int outreachDurationDays) {
        return outreachService.updatePlantOutreachData(id, outreachDurationDays);
    }
}