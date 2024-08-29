package com.game.mapper;

import com.game.entity.Outreach;
import com.game.entity.Plant;
import com.game.entity.SpecialOutreach;
import com.game.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into users (email, lastname, firstname)" +
            "values (#{email}, #{lastName}, #{firstName})")
    void createUser(String email, String lastName, String firstName);

    @Update("update users " +
            "set email=#{email}, lastName=#{lastName}, firstName=#{firstName}, " +
            "where id=#{id}")
    void updateUser(int id, String email, String lastName, String firstName);


    @Select("select * from users where id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "lastName", column = "lastname"),
            @Result(property = "firstName", column = "firstname")
    })
    User getUserById(int id);
}
