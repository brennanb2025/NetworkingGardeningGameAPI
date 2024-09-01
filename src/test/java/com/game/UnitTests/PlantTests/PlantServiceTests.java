package com.game.UnitTests.PlantTests;

import com.game.dao.OutreachDao;
import com.game.dao.PlantDao;
import com.game.entity.Outreach;
import com.game.entity.Plant;
import com.game.service.PlantService;
import com.game.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlantServiceTests {

    @Mock
    private PlantDao plantDao;

    @Mock
    private OutreachDao outreachDao;

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
        int plantType = 1;
        String name = "John Doe";
        String notes = "Notes on John Doe.";
        int outreachDurationDays = 60;
        LocalDate nextOutreachTime = LocalDate.now().plusDays(10);
        short stage = 1;
        int xCoord = 10;
        int yCoord = 10;

        doNothing().when(plantDao).addPlant(userId, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);

        // Act
        plantService.addPlant(userId, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);

        // Assert
        verify(plantDao).addPlant(userId, plantType, name, notes, outreachDurationDays,
                nextOutreachTime, stage, xCoord, yCoord);
    }

    @Test
    public void testUpdatePlantInfo_Success_PlantExists() {
        // Arrange
        long id = 1;
        String name = "John Doe";
        String notes = "Notes on John Doe.";
        boolean success = true;

        when(plantDao.updatePlantInfo(id, name, notes)).thenReturn(1);

        // Act
        boolean result = plantService.updatePlantInfo(id, name, notes);

        // Assert
        assertEquals(result, success);
        verify(plantDao).updatePlantInfo(id, name, notes);
    }

    @Test
    public void testUpdatePlantInfo_Failure_PlantDoesNotExist() {
        // Arrange
        long id = 1;
        String name = "John Doe";
        String notes = "Notes on John Doe.";
        boolean failure = false;

        when(plantDao.updatePlantInfo(id, name, notes)).thenReturn(0);

        // Act
        boolean result = plantService.updatePlantInfo(id, name, notes);

        // Assert
        assertEquals(result, failure);
        verify(plantDao).updatePlantInfo(id, name, notes);
    }

    @Test
    public void testGetPlantById_PlantExists() {
        // Arrange
        long id = 1;
        Plant plant = new Plant(id, 1L, 1, "John Doe", "Notes on John Doe.", 60,
                LocalDate.now().plusDays(10), (short)1, 10, 10, LocalDate.now());

        when(plantDao.getPlantById(id)).thenReturn(plant);
        // This is a Mockito setup line. It instructs Mockito to return the plant object
        // when the getPlantById method of plantDao is called with the id as an argument.
        // This mocks the behavior of the plantDao without actually hitting the database.

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

    @Test
    public void testUpdatePlantOutreachData_LaterTime_Success_NoSpecialOutreach_UpdatesToNewNormalOutreachTime() {
        // update outreach time from 5 days to 10 day with the last outreach time (normal outreach) being now
        // compare that with last special outreach time (here none)
        // should update to last outreach time (now) + 10 days

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 5;
        int newOutreachDurationDays = 10;
        LocalDate lastOutreachTime = LocalDate.now();

        // next outreach time shouldn't matter - set it to the last outreach time + current duration days
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, lastOutreachTime.plusDays(outreachDurationDays), (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // make a new lastOutreach - have it set to now.
        Outreach lastOutreach = new Outreach(1, plantId, userId,
                lastOutreachTime, "Last outreach contents");

        // no special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(null);

        // no next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(null);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(lastOutreach);

        // Act
        plantService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        // will update next outreach time to last normal outreach (now) + new duration
        verify(plantDao).updatePlantOutreachData(plantId,
                newOutreachDurationDays,
                lastOutreachTime.plusDays(newOutreachDurationDays));
    }

    @Test
    public void testUpdatePlantOutreachData_SoonerTime_Success_NoSpecialOutreach_UpdatesToNewNormalOutreachTime() {
        // update outreach time from 5 days to 1 day with the last outreach time (normal outreach) being now
        // compare that with last special outreach time (here none)
        // should update to last outreach time (now) + 1 day

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 5;
        int newOutreachDurationDays = 1;
        LocalDate lastOutreachTime = LocalDate.now();

        // next outreach time shouldn't matter - set it to the last outreach time + current duration days
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, lastOutreachTime.plusDays(outreachDurationDays), (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // make a new lastOutreach - have it set to now.
        Outreach lastOutreach = new Outreach(1, plantId, userId,
                lastOutreachTime, "Last outreach contents");

        // no special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(null);

        // no next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(null);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(lastOutreach);

        // Act
        plantService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        // will update next outreach time to last normal outreach (now) + new duration
        verify(plantDao).updatePlantOutreachData(plantId,
                newOutreachDurationDays,
                lastOutreachTime.plusDays(newOutreachDurationDays));
    }

    @Test
    public void testUpdatePlantOutreachData_SoonerTime_Success_NoPastOutreaches_DoesNotUpdateOutreachTime() {
        // update outreach time from 5 days to 1 day with no past outreaches
        // should update the duration but not the next outreach time

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 5;
        int newOutreachDurationDays = 1;

        // next outreach time does matter. Set it to now + 1 day.
        LocalDate nextOutreachTime = LocalDate.now().plusDays(1);
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, nextOutreachTime, (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // no special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(null);

        // no next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(null);

        // no normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(null);

        // Act
        plantService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        // will update duration days but not next outreach time
        verify(plantDao).updatePlantOutreachData(plantId,
                newOutreachDurationDays,
                nextOutreachTime);
    }

    @Test
    public void testUpdatePlantOutreachData_Failure_PlantDoesNotExist() {
        // Arrange
        long id = 1;
        boolean success = false;

        when(plantDao.getPlantById(id)).thenReturn(null);

        // Act
        boolean result = plantService.updatePlantOutreachData(id, 1);

        // Assert
        assertEquals(result, success);
        // verify it wasn't called
        verify(plantDao, times(0)).updatePlantOutreachData(anyLong(), anyInt(), any(LocalDate.class));
    }
}