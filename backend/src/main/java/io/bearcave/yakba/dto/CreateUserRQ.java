package io.bearcave.yakba.dto;

import lombok.Data;

@Data
public class CreateUserRQ {
    private String username;
    private String email;
    private String password;
}
