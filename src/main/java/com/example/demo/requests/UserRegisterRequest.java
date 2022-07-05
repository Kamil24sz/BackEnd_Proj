package com.example.demo.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private boolean enabled;
    private boolean tokenExpired;

}
