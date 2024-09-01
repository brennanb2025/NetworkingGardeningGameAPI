package com.game.service;

import com.game.entity.User;
import com.game.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public void createUser(String email, String lastName, String firstName) {
        userDao.createUser(email, lastName, firstName);
    }

    public boolean updateUser(int id, String email, String lastName, String firstName){
        return userDao.updateUser(id, email, lastName, firstName) == 1;
    }

    public User getUserById(int id){
        return userDao.getUserById(id);
    }
}
