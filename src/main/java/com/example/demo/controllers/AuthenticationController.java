package com.example.demo.controllers;

import com.example.demo.requests.UserLoginRequest;
import com.example.demo.requests.UserRegisterRequest;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.MailService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {


    final private AuthenticationService authenticationService;

    final private UserService userService;

    @RequestMapping(
            value = "/api/user/check",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    @ResponseBody
    public String checkUser(
            HttpServletRequest httpServletRequest,
            @RequestBody UserRegisterRequest userRegisterRequest
    ){
        if(this.authenticationService.checkUser( userRegisterRequest.getEmail()))
            return "User already logged in";

        return "User not logged in!";
    }

    @RequestMapping(
            value = "/api/user/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST
    )
    @ResponseBody
    public String registerUser(
            HttpServletRequest httpServletRequest,
            @RequestBody UserRegisterRequest userRegisterRequest
    ){
        if(authenticationService.checkIfUserExists(userRegisterRequest.getEmail()))
            return "Provided email address already exists!";

        if(this.authenticationService.checkUser(userRegisterRequest.getEmail()))
            return "Failed! (User already logged in)";

        try {
            if(this.userService.createNewUser(userRegisterRequest)) {
                return "Success!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            String error = e.toString(); //TODO log

            return "Failed (try again later) \n"+error;
        }

        return "Failed!";
    }
    @RequestMapping(
            value = "api/user/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String loginUser(
            HttpServletRequest httpServletRequest,
            @RequestBody UserLoginRequest userLoginRequest
    ){
        if(this.authenticationService.checkUser(userLoginRequest.getEmail()))
            return "Failed! (User already logged in)";

        if(this.authenticationService.loginUser(httpServletRequest, userLoginRequest))
            return "Success!";

        return "Failed!";
    }

    @RequestMapping(
            value = "api/user/logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String logoutUser(
            HttpServletRequest httpServletRequest,
            @RequestBody UserLoginRequest userLoginRequest
    ){
        if(this.authenticationService.logoutUser(httpServletRequest, userLoginRequest.getEmail()))
            return "Success!";

        return "Failed!";
    }
}
