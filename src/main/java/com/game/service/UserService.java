package com.game.service;

import com.game.entity.Outreach;
import com.game.entity.Plant;
import com.game.entity.SpecialOutreach;
import com.game.entity.User;
import com.game.dao.UserDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public void createUser(String email, String lastName, String firstName) throws IOException {
        userDao.createUser(email, lastName, firstName);
    }

    public void updateUser(int id, String email, String lastName, String firstName){
        userDao.updateUser(id, email, lastName, firstName);
    }

    public User getUserById(int id){
        User user = userDao.getUserById(id);
        if(user == null) {
            return null;
        }
        return user;
    }
}
