package com.game.UnitTests.PlantTests;

import com.game.dao.OutreachDao;
import com.game.dao.PlantDao;
import com.game.entity.*;
import com.game.service.plant.PlantService;
import com.game.service.plant.Operations.AddPlantOperation;
import com.game.service.plant.Operations.UpdatePlantInfoOperation;
import com.game.service.plant.Operations.UpdatePlantCoordinatesOperation;
import com.game.service.plant.Operations.UpdatePlantTypeAndCoordinatesOperation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlantServiceTests {

    @Mock
    private PlantDao plantDao;

    @Mock
    private OutreachDao outreachDao;

    @Mock
    private AddPlantOperation addPlantOperation;

    @Mock
    private UpdatePlantInfoOperation updatePlantInfoOperation;

    @Mock
    private UpdatePlantCoordinatesOperation updatePlantCoordinatesOperation;

    @Mock
    private UpdatePlantTypeAndCoordinatesOperation updatePlantTypeAndCoordinatesOperation;

    @InjectMocks
    private PlantService plantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePlant() throws IOException {
        // Arrange
        long userId = 1;
        long createdPlantId = 2;
        int plantType = 1;
        String name = "John Doe";
        String notes = "Notes on John Doe.";
        int outreachDurationDays = 60;
        LocalDate nextOutreachTime = LocalDate.now().plusDays(10);
        short stage = 1;
        int xCoord = 10;
        int yCoord = 10;

        when(addPlantOperation.execute(userId, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord)).thenReturn(createdPlantId);

        // Act
        long actualCreatedPlantId = plantService.addPlant(userId, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);

        // Assert
        assertEquals(createdPlantId, actualCreatedPlantId);
        verify(addPlantOperation).execute(userId, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);
    }

    @Test
    public void testUpdatePlantInfo_Success_PlantExists() {
        // Arrange
        long id = 1;
        String name = "John Doe";
        String notes = "Notes on John Doe.";
        boolean success = true;

        when(updatePlantInfoOperation.execute(id, name, notes)).thenReturn(true);

        // Act
        boolean result = plantService.updatePlantInfo(id, name, notes);

        // Assert
        assertEquals(result, success);
        verify(updatePlantInfoOperation).execute(id, name, notes);
    }

    @Test
    public void testUpdatePlantInfo_Failure_PlantDoesNotExist() {
        // Arrange
        long id = 1;
        String name = "John Doe";
        String notes = "Notes on John Doe.";
        boolean failure = false;

        when(updatePlantInfoOperation.execute(id, name, notes)).thenReturn(false);

        // Act
        boolean result = plantService.updatePlantInfo(id, name, notes);

        // Assert
        assertEquals(result, failure);
        verify(updatePlantInfoOperation).execute(id, name, notes);
    }

    @Test
    public void testGetPlantById_PlantExists() {
        // Arrange
        long id = 1;
        Plant plant = new Plant(id, 1L, 1, "John Doe", "Notes on John Doe.", 60,
                LocalDate.now().plusDays(10), (short)1, 10, 10, LocalDate.now());

        when(plantDao.getPlantById(id)).thenReturn(plant);

        // Act
        Plant result = plantService.getPlantById(id);

        // Assert
        assertEquals(plant, result);
        verify(plantDao).getPlantById(id);
    }

    @Test
    public void testGetPlantById_PlantDoesNotExist() {
        // Arrange
        int id = 2;
        when(plantDao.getPlantById(id)).thenReturn(null);

        // Act
        Plant result = plantService.getPlantById(id);

        // Assert
        assertNull(result);
        verify(plantDao).getPlantById(id);
    }

    @Test
    public void testGetPlantsByUserId_TwoResults() {
        // Arrange
        long userId = 1;
        Plant plant1 = new Plant(1, userId, 1, "John Doe", "Notes on John Doe.",
                60, LocalDate.now().plusDays(10), (short)1,
                10, 10, LocalDate.now());
        Plant plant2 = new Plant(2, userId, 1, "Jane Smith", "Notes on Jane Smith.",
                60, LocalDate.now().plusDays(20), (short)1,
                10, 10, LocalDate.now());
        List<Plant> plantList = List.of(plant1, plant2);
        when(plantDao.getPlantsByUserId(userId)).thenReturn(plantList);

        // Act
        List<Plant> result = plantService.getPlantsByUserId(userId);

        // Assert
        assertEquals(result, plantList);
        verify(plantDao).getPlantsByUserId(userId);
    }

    @Test
    public void testGetPlantsByUserId_NoUser_NoResults() {
        // Arrange
        long nonExistentUserId = 2;
        List<Plant> emptyPlantList = Collections.emptyList();
        when(plantDao.getPlantsByUserId(nonExistentUserId)).thenReturn(emptyPlantList);

        // Act
        List<Plant> result = plantService.getPlantsByUserId(nonExistentUserId);

        // Assert
        assertEquals(result, emptyPlantList);
        verify(plantDao).getPlantsByUserId(nonExistentUserId);
    }

    @Test
    public void testGetPlantsByUserId_UserExists_NoResults() {
        // Arrange
        long userId = 1;
        List<Plant> emptyPlantList = Collections.emptyList();
        when(plantDao.getPlantsByUserId(userId)).thenReturn(emptyPlantList);

        // Act
        List<Plant> result = plantService.getPlantsByUserId(userId);

        // Assert
        assertEquals(result, emptyPlantList);
        verify(plantDao).getPlantsByUserId(userId);
    }
}
