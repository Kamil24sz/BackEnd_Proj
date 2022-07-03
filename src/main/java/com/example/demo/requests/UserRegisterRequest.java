package com.example.demo.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private boolean enabled;
    private boolean tokenExpired;

    public UserRegisterRequest(){

    }

    public UserRegisterRequest(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        enabled = true;
        tokenExpired = true;
    }

}
