package com.game.controller;

import com.game.entity.User;
import com.game.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RestController
public class UserController {

    @Autowired
    UserService userService;

    // add user
    @PostMapping("/createuser")
    public void createUser(@RequestParam("email") String email, @RequestParam("lastname") String lastName,
                        @RequestParam("firstname") String firstName){
        userService.createUser(email, lastName, firstName);
    }

    // update user
    @PutMapping("/updateuser")
    public boolean updateUser(@RequestParam("userid") int id, @RequestParam("email") String email,
                           @RequestParam("lastname") String lastName, @RequestParam("firstname") String firstName){
        return userService.updateUser(id, email, lastName, firstName);
    }

    // get user by id
    @GetMapping("/getuser")
    public User getUserById(@RequestParam("userid") int userid){
        return userService.getUserById(userid);
    }
}
