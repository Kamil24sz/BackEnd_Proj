package com.example.demo.controllers;

import com.example.demo.models.UserEntity;
import com.example.demo.requests.UserRegisterRequest;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@Validated
public class UsersController {

    private final UserService userService;

    private final AuthenticationService authenticationService;


    @GetMapping("/api/users")
    public ResponseEntity<Page<UserEntity>> getUsersPaginated(
            @RequestParam(name = "page-number", defaultValue = "1") @Min(1) int pageNumber,
            @RequestParam(name = "page-size",defaultValue = "1") @Min(1) @Max(100) int pageSize) {
        return ResponseEntity.ok(userService.getUsersPaginated(pageNumber,pageSize));
    }

    @RequestMapping(
            value = "/api/user/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        userService.createNewUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserEntity> findUser(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return null;

        return userService.findUser(id);
    }

    @RequestMapping(
            value = "/api/users/{id}/remove",
            method ={RequestMethod.DELETE, RequestMethod.GET},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        return userService.deleteUser(id);
    }

    @RequestMapping("/api/admin/user/promote/{id}")
    @ResponseBody
    public String promoteUser(@PathVariable long id){
        if(authenticationService.promoteUser(id))
            return "Users privilege promoted successfully!";
        return "Failed to promote user privilege!";
    }

    @RequestMapping("/api/admin/user/degrade/{id}")
    @ResponseBody
    public String degradeUser(@PathVariable long id){
        if(authenticationService.degradeUser(id))
            return "Users privilege degraded successfully!";
        return "Failed to degrade user privilege!";
    }

    @RequestMapping("/api/admin/user/remove/{id}")
    @ResponseBody
    public String deletedUser(HttpServletRequest httpServletRequest, @PathVariable long id){
        if (httpServletRequest.getSession().getAttributeNames().hasMoreElements() == false)
            return "Admin not login in!";

        if(authenticationService.deleteUser(id))
            return "Users deleted successfully!";
        return "Failed to delete user!";
    }
}