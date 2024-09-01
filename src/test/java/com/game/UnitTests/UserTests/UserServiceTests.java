package com.game.UnitTests.UserTests;

import com.game.dao.UserDao;
import com.game.service.UserService;
import com.game.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        // Arrange
        String email = "john.doe@example.com";
        String lastName = "Doe";
        String firstName = "John";

        doNothing().when(userDao).createUser(email, lastName, firstName);

        // Act
        userService.createUser(email, lastName, firstName);

        // Assert
        verify(userDao).createUser(email, lastName, firstName);
    }

    @Test
    public void testUpdateUser_Success_UserExists() {
        // Arrange
        int id = 1;
        String email = "jane.doe@example.com";
        String lastName = "Doe";
        String firstName = "Jane";
        boolean success = true;

        // Simulate the userDao.updateUser returning 1, meaning 1 row was updated
        when(userDao.updateUser(id, email, lastName, firstName)).thenReturn(1);

        // Act
        boolean result = userService.updateUser(id, email, lastName, firstName);

        // Assert
        assertEquals(result, success);
        verify(userDao).updateUser(id, email, lastName, firstName);
    }

    @Test
    public void testUpdateUser_Failure_UserDoesNotExist() {
        // Arrange
        int id = 1;
        String email = "jane.doe@example.com";
        String lastName = "Doe";
        String firstName = "Jane";
        boolean failure = false;

        // Simulate the userDao.updateUser returning 0, meaning 0 rows were updated
        when(userDao.updateUser(id, email, lastName, firstName)).thenReturn(0);

        // Act
        boolean result = userService.updateUser(id, email, lastName, firstName);

        // Assert
        assertEquals(result, failure);
        verify(userDao).updateUser(id, email, lastName, firstName);
    }

    @Test
    public void testGetUserById_UserExists() {
        // Arrange
        int id = 1;
        User user = new User(id, "John", "Doe", "john.doe@example.com", LocalDate.now());
        when(userDao.getUserById(id)).thenReturn(user);

        // Act
        User result = userService.getUserById(id);

        // Assert
        assertEquals(user, result);
        verify(userDao).getUserById(id);
    }

    @Test
    public void testGetUserById_UserDoesNotExist() {
        // Arrange
        int id = 2;
        when(userDao.getUserById(id)).thenReturn(null);

        // Act
        User result = userService.getUserById(id);

        // Assert
        assertNull(result);
        verify(userDao).getUserById(id);
    }
}
