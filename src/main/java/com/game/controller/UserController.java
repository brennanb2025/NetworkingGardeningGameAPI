package com.game.controller;

import com.game.entity.Outreach;
import com.game.entity.Plant;
import com.game.entity.SpecialOutreach;
import com.game.entity.User;
import com.game.service.OutreachService;
import com.game.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RestController
public class UserController {

    @Autowired
    UserService userService;

    // add user
    @PostMapping("/createuser")
    public ResponseEntity<String> createUser(@RequestParam("email") String email, @RequestParam("lastname") String lastName,
                        @RequestParam("firstname") String firstName){
        try {
            userService.createUser(email, lastName, firstName);
            return ResponseEntity.ok().build(); // Return 200 OK if successful
        } catch(IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating user: " + e.getMessage());
        }
    }

    // update user
    @PutMapping("/updateuser")
    public void updateUser(@RequestParam("userid") int id, @RequestParam("email") String email,
                           @RequestParam("lastname") String lastName, @RequestParam("firstname") String firstName){
        userService.updateUser(id, email, lastName, firstName);
    }

    // get user by id
    @GetMapping("/getuser")
    public User getUserById(@RequestParam("userid") int userid){
        return userService.getUserById(userid);
    }
}
