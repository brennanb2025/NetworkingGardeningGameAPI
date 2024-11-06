package com.game.IntegrationTests.PlantTests;

import com.game.entity.Plant;
import com.game.service.PlantService;
import com.game.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class PlantIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlantService plantService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        // Insert test data into in-memory database

        // insert user
        // userService.createUser("brennan@gmail.com", "Brennan", "Benson");

        // insert plant
        plantService.addPlant(1L, 1, "John Smith", "Bday: March 15", 10, LocalDate.now(), (short) 1, 5, 5);
    }

    @Test
    public void testAddPlant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/addplant")
                        .param("userId", "1")
                        .param("plantType", "1")
                        .param("name", "John Smith")
                        .param("notes", "Bday: March 15")
                        .param("outreachDurationDays", "10")
                        .param("nextOutreachTime", LocalDate.now().toString())
                        .param("stage", "1")
                        .param("xCoord", "5")
                        .param("yCoord", "5"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPlantsByUserId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/getplantsbyuserid")
                        .param("userid", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"));
    }

    @Test
    public void testGetPlantById() throws Exception {
        Plant plant = plantService.getPlantsByUserId(1L).get(0);
        mockMvc.perform(MockMvcRequestBuilders.get("/getplantbyid")
                        .param("id", String.valueOf(plant.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"));
    }

    @Test
    public void testUpdatePlantInfo() throws Exception {
        Plant plant = plantService.getPlantsByUserId(1L).get(0);
        mockMvc.perform(MockMvcRequestBuilders.put("/updateplantinfo")
                        .param("id", String.valueOf(plant.getId()))
                        .param("name", "Updated John Smith name")
                        .param("notes", "updated Bday: March 16"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testUpdatePlantOutreachDurationDays() throws Exception {
        Plant plant = plantService.getPlantsByUserId(1L).get(0);
        mockMvc.perform(MockMvcRequestBuilders.put("/updateplantoutreachdurationdays")
                        .param("id", String.valueOf(plant.getId()))
                        .param("outreachDurationDays", "20"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testUpdatePlantCoordinates() throws Exception {
        Plant plant = plantService.getPlantsByUserId(1L).get(0);
        mockMvc.perform(MockMvcRequestBuilders.put("/updateplantcoordinates")
                        .param("id", String.valueOf(plant.getId()))
                        .param("x", "10")
                        .param("y", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testUpdatePlantTypeAndCoordinates() throws Exception {
        Plant plant = plantService.getPlantsByUserId(1L).get(0);
        mockMvc.perform(MockMvcRequestBuilders.put("/updateplanttypeandcoordinates")
                        .param("id", String.valueOf(plant.getId()))
                        .param("plantType", "3")
                        .param("x", "12")
                        .param("y", "12"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
