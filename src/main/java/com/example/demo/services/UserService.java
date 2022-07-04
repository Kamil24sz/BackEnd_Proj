package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.models.repos.RoleRepository;
import com.example.demo.models.UserEntity;
import com.example.demo.models.repos.UserRepository;
import com.example.demo.requests.UserLoginRequest;
import com.example.demo.requests.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public Page<UserEntity> getUsersPaginated(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1,pageSize);
        return userRepository.findAll(pageRequest);
    }

    public List<UserEntity> addUsers() {
        if(userRepository.findByEmail("kamil240sz@gmail.com") != null)
            return null;
        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity());
        users.add(new UserEntity());
        users.add(new UserEntity());

        users.get(0).setFirstname("Kamil");
        users.get(0).setLastname("Szczypczyk");
        users.get(0).setEmail("kamil240sz@gmail.com");

        users.get(1).setFirstname("Adam");
        users.get(1).setLastname("Kowalski");
        users.get(1).setEmail("akowalski@gmail.com");

        users.get(2).setFirstname("Piotr");
        users.get(2).setLastname("Nowak");
        users.get(2).setEmail("pnowak@gmail.com");

        for (int x = 0; x<3; x++){
            users.get(x).setPassword(bCryptPasswordEncoder.encode("password"));
            users.get(x).setEnabled(true);
            Role role = roleRepository.findByName("ROLE_USER");
            users.get(x).setRoles(Arrays.asList(role));
        }

        return userRepository.saveAllAndFlush(users);
    }

    public boolean checkIfUserExists(String email){
        List<UserEntity> users = userRepository.findByEmail(email);
        if(users.size()>0)
            return true;
        return false;
    }

    public boolean login (UserLoginRequest userLoginRequest){
        if (checkIfUserExists(userLoginRequest.getEmail())){
            List<UserEntity> users = userRepository.findByEmail(userLoginRequest.getEmail());
            if(bCryptPasswordEncoder.matches(userLoginRequest.getPassword(), users.get(0).getPassword())) {

                return true;
            }
        }

        return false;
    }

    public boolean createNewUser(UserRegisterRequest userRegisterRequest){
        UserEntity user = new UserEntity();
        user.setEmail(userRegisterRequest.getEmail());
        user.setFirstname(userRegisterRequest.getFirstname());
        user.setLastname(userRegisterRequest.getLastname());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()));
        user.setEnabled(true);
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(role));
        userRepository.saveAndFlush(user);
        return true;
    }
    public UserEntity createNewUser(UserEntity user){
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(role));
        return userRepository.saveAndFlush(user);
    }

    public ResponseEntity<UserEntity> findUser(long id){
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> deleteUser(long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<String>("{\"result\": true}", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"result\": false}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}