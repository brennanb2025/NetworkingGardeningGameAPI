package com.game.dao;

import com.game.entity.Outreach;
import com.game.entity.Plant;
import com.game.entity.SpecialOutreach;
import com.game.entity.User;
import com.game.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class UserDao {

    @Autowired
    UserMapper userMapper;

    public void createUser(String email, String lastName, String firstName){
        userMapper.createUser(email, lastName, firstName);
    }

    public int updateUser(int id, String email, String lastName, String firstName){
        return userMapper.updateUser(id, email, lastName, firstName);
    }

    public User getUserById(int id){
        return userMapper.getUserById(id);
    }
}
