package com.game.UnitTests.OutreachTests;

import com.game.dao.OutreachDao;
import com.game.dao.PlantDao;
import com.game.entity.*;
import com.game.service.outreach.OutreachService;
import com.game.service.outreach.Operations.UpdatePlantOutreachDataOperation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OutreachServiceTests {

    @Mock
    private PlantDao plantDao;

    @Mock
    private OutreachDao outreachDao;

    @Mock
    private UpdatePlantOutreachDataOperation updatePlantOutreachDataOperation;

    @InjectMocks
    private OutreachService outreachService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
    }

    @Test
    public void testUpdatePlantOutreachData_SoonerTime_Success_NoPastSpecialOutreach_NoFutureSpecialOutreach_UpdatesToNewNormalOutreachTime() {
        // update outreach time from 5 days to 1 day with the last outreach time (normal outreach) being now
        // compare that with next special outreach time (here none)
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

        // no past special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(null);

        // no next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(null);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(lastOutreach);

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
    }

    @Test
    public void testUpdatePlantOutreachData_SoonerTime_Success_NoPastSpecialOutreach_FutureSpecialOutreach_UpdatesToNewNormalOutreachTime() {
        // update outreach time from 5 days to 1 day with next time set to 2 days (no past outreaches)
        // compare that with next special outreach time (in 3 days)
        // should retain same next time (2 days)

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 5;
        int newOutreachDurationDays = 1;
        LocalDate nextOutreachTime = LocalDate.now().plusDays(2);
        LocalDate nextSpecialOutreachTime = LocalDate.now().plusDays(3);

        // next outreach time: set initially to
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, nextOutreachTime, (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // no past special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(null);

        SpecialOutreach nextSpecialOutreach = new SpecialOutreach(1, plantId, userId,
                nextSpecialOutreachTime, "notes here", false);
        // yes next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(nextSpecialOutreach);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(null);

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
    }

    @Test
    public void testUpdatePlantOutreachData_LaterTime_Success_NoPastSpecialOutreach_FutureSpecialOutreach_UpdatesToNextSpecialOutreachTime() {
        // update outreach time from 1 day to 5 days with the last outreach time (normal outreach) being now
        // compare next normal outreach time (in 5 day) with next special outreach time, in 3 days
        // should update to next special outreach time (in 3 days)

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 1;
        int newOutreachDurationDays = 5;
        LocalDate lastOutreachTime = LocalDate.now();
        LocalDate nextSpecialOutreachTime = LocalDate.now().plusDays(3);

        // next outreach time shouldn't matter - set it to the last outreach time + current duration days
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, lastOutreachTime.plusDays(outreachDurationDays), (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // make a new lastOutreach - have it set to now.
        Outreach lastOutreach = new Outreach(1, plantId, userId,
                lastOutreachTime, "Last outreach contents");

        // no past special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(null);

        SpecialOutreach nextSpecialOutreach = new SpecialOutreach(1, plantId, userId,
                nextSpecialOutreachTime, "notes here", false);
        // yes next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(nextSpecialOutreach);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(lastOutreach);

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
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

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
    }

    @Test
    public void testUpdatePlantOutreachData_SoonerTime_Success_PastSpecialOutreachAndNormalOutreach_FutureSpecialOutreach_UpdatesToPastSpecialPlusNewDuration() {
        // update outreach time from 5 days to 1 day
        // special outreach was 2 days ago
        // normal outreach time was 3 days ago
        // compare next normal outreach time (2 days ago + 1 day = -1 days) with next special outreach time, in 2 days
        // should update to next normal outreach time (in -1 days)

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 5;
        int newOutreachDurationDays = 1;
        LocalDate lastSpecialOutreachTime = LocalDate.now().plusDays(-2);
        LocalDate lastNormalOutreachTime = LocalDate.now().plusDays(-3);
        LocalDate nextSpecialOutreachTime = LocalDate.now().plusDays(2);

        // next outreach time shouldn't matter - set it to the last outreach time + current duration days
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, lastSpecialOutreachTime.plusDays(outreachDurationDays), (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // make a new lastOutreach - have it set to now.
        Outreach lastOutreach = new Outreach(1, plantId, userId,
                lastNormalOutreachTime, "Last outreach contents");
        SpecialOutreachCompletionRecord lastSpecialOutreach = new SpecialOutreachCompletionRecord(
                1L, 1L, lastSpecialOutreachTime, "notes"
        );

        // yes past special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(lastSpecialOutreach);

        SpecialOutreach nextSpecialOutreach = new SpecialOutreach(1, plantId, userId,
                nextSpecialOutreachTime, "notes here", false);
        // yes next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(nextSpecialOutreach);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(lastOutreach);

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
    }

    @Test
    public void testUpdatePlantOutreachData_SoonerTime_Success_PastSpecialOutreachAndNormalOutreach_FutureSpecialOutreach_UpdatesToPastNormalPlusNewDuration() {
        // update outreach time from 5 days to 1 day
        // special outreach was 7 days ago
        // normal outreach time was 2 days ago
        // compare next normal outreach time (2 days ago + 1 day = -1 days) with next special outreach time, in 2 days
        // should update to next normal outreach time (in -1 days)

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 5;
        int newOutreachDurationDays = 1;
        LocalDate lastSpecialOutreachTime = LocalDate.now().plusDays(-7);
        LocalDate lastNormalOutreachTime = LocalDate.now().plusDays(-2);
        LocalDate nextSpecialOutreachTime = LocalDate.now().plusDays(2);

        // next outreach time shouldn't matter - set it to the last outreach time + current duration days
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, lastSpecialOutreachTime.plusDays(outreachDurationDays), (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // make a new lastOutreach - have it set to now.
        Outreach lastOutreach = new Outreach(1, plantId, userId,
                lastNormalOutreachTime, "Last outreach contents");
        SpecialOutreachCompletionRecord lastSpecialOutreach = new SpecialOutreachCompletionRecord(
                1L, 1L, lastSpecialOutreachTime, "notes"
        );

        // yes past special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(lastSpecialOutreach);

        SpecialOutreach nextSpecialOutreach = new SpecialOutreach(1, plantId, userId,
                nextSpecialOutreachTime, "notes here", false);
        // yes next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(nextSpecialOutreach);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(lastOutreach);

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
    }

    @Test
    public void testUpdatePlantOutreachData_SoonerTime_Success_PastSpecialOutreachAndNormalOutreach_FutureSpecialOutreach_UpdatesToFuture() {
        // update outreach time from 5 days to 4 days
        // special outreach was 2 days ago
        // normal outreach time was 4 days ago
        // compare next normal outreach time (2 days ago + 4 day = 2 days) with next special outreach time, in 1 day
        // should update to next special outreach time (in 1 day)

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 5;
        int newOutreachDurationDays = 4;
        LocalDate lastSpecialOutreachTime = LocalDate.now().plusDays(-2);
        LocalDate lastNormalOutreachTime = LocalDate.now().plusDays(-4);
        LocalDate nextSpecialOutreachTime = LocalDate.now().plusDays(1);

        // next outreach time shouldn't matter - set it to the last outreach time + current duration days
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, lastSpecialOutreachTime.plusDays(outreachDurationDays), (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // make a new lastOutreach - have it set to now.
        Outreach lastOutreach = new Outreach(1, plantId, userId,
                lastNormalOutreachTime, "Last outreach contents");
        SpecialOutreachCompletionRecord lastSpecialOutreach = new SpecialOutreachCompletionRecord(
                1L, 1L, lastSpecialOutreachTime, "notes"
        );

        // yes past special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(lastSpecialOutreach);

        SpecialOutreach nextSpecialOutreach = new SpecialOutreach(1, plantId, userId,
                nextSpecialOutreachTime, "notes here", false);
        // yes next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(nextSpecialOutreach);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(lastOutreach);

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
    }

    @Test
    public void testUpdatePlantOutreachData_SoonerTime_Success_PastSpecialOutreachAndNormalOutreach_NoFutureSpecialOutreach_UpdatesToPastSpecialPlusNewDuration() {
        // update outreach time from 5 days to 4 days
        // special outreach was 2 days ago
        // normal outreach time was 4 days ago
        // compare next normal outreach time (2 days ago + 4 days = 2 days) with next special outreach time, null
        // should update to special outreach + new duration

        // Arrange
        long plantId = 1;
        long userId = 1;
        int outreachDurationDays = 5;
        int newOutreachDurationDays = 4;
        LocalDate lastSpecialOutreachTime = LocalDate.now().plusDays(-2);
        LocalDate lastNormalOutreachTime = LocalDate.now().plusDays(-4);

        // next outreach time shouldn't matter - set it to the last outreach time + current duration days
        Plant plant = new Plant(plantId, userId, 1, "John Doe", "Notes on John Doe.",
                outreachDurationDays, lastSpecialOutreachTime.plusDays(outreachDurationDays), (short)1,
                10, 10, LocalDate.now());
        when(plantDao.getPlantById(plantId)).thenReturn(plant);

        // make a new lastOutreach - have it set to now.
        Outreach lastOutreach = new Outreach(1, plantId, userId,
                lastNormalOutreachTime, "Last outreach contents");
        SpecialOutreachCompletionRecord lastSpecialOutreach = new SpecialOutreachCompletionRecord(
                1L, 1L, lastSpecialOutreachTime, "notes"
        );

        // yes past special outreach
        when(outreachDao.getLastSpecialOutreachCompletionRecordByPlantId(plantId)).thenReturn(lastSpecialOutreach);

        // no next special outreach time
        when(outreachDao.getNextSpecialOutreachByPlantId(plantId)).thenReturn(null);

        // yes normal outreach
        when(outreachDao.getLastOutreach(plantId)).thenReturn(lastOutreach);

        when(updatePlantOutreachDataOperation.execute(plantId, newOutreachDurationDays)).thenReturn(true);

        // Act
        boolean result = outreachService.updatePlantOutreachData(plantId, newOutreachDurationDays);

        // Assert
        assertEquals(true, result);
        verify(updatePlantOutreachDataOperation).execute(plantId, newOutreachDurationDays);
    }

    @Test
    public void testUpdatePlantOutreachData_Failure_PlantDoesNotExist() {
        // Arrange
        long id = 1;
        boolean success = false;

        when(plantDao.getPlantById(id)).thenReturn(null);

        // Act
        boolean result = outreachService.updatePlantOutreachData(id, 1);

        // Assert
        assertEquals(result, success);
        // verify it wasn't called
        verify(plantDao, times(0)).updatePlantOutreachData(anyLong(), anyInt(), any(LocalDate.class));
    }
}
