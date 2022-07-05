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

    @GetMapping("/api/users/add")
    public ResponseEntity<List<UserEntity>> addUsers() {
        if(userService.addUsers() == null) {
            UserEntity user = new UserEntity(0L,"Użytkownicy już istnieją","Użytkownicy już istnieją","","",false,false,null);
            ArrayList<UserEntity> array = new ArrayList<>();
            array.add(user);
            return ResponseEntity.ok(array);
        }

        return ResponseEntity.ok(userService.addUsers());
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
    public ResponseEntity<UserEntity> findUser(@PathVariable Long id) {
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

    @RequestMapping(
            value = "/api/admin/user/promote",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> promoteUser(@RequestBody UserRegisterRequest user) {

        if(authenticationService.promoteUser(user))
            return ResponseEntity.ok("User promotion successful");

        return ResponseEntity.ok("User promotion Failed");
    }
}