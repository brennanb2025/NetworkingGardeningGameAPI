package com.game.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    long id;
    String email;
    String lastName;
    String firstName;
    LocalDate creationDatetime;
}
