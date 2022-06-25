package com.example.demo.services;

import com.example.demo.models.UserEntity;
import com.example.demo.models.UserRepository;
import com.example.demo.requests.UserLoginRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void onCreate() {
        // spring.jpa.hibernate.ddl-auto= update
    }

    @PreDestroy
    private void onDestroy() {
        // spring.jpa.hibernate.ddl-auto= update
    }

    public Page<UserEntity> getUsersPaginated(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1,pageSize);
        return userRepository.findAll(pageRequest);
    }

    public List<UserEntity> addUsers() {
        List<UserEntity> users = new ArrayList<>();
        //users.add(new UserEntity(null, "Kamil", "Szczypczyk", "kamil240sz@gmail.com", "admin");
        //users.add(new UserEntity(null, "Adam","Nowak","anowak@wsei.pl","password1"));
        //users.add(new UserEntity( null,"Piotr","Kowalczyk","pkowalczyk@wsei.pl", "password2"));
        UserEntity testuser = UserEntity.builder()
                .firstname("Kamil")
                .password("haslo")
                .build();
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
            if(users.get(0).getPassword().equals(userLoginRequest.getPassword()))
                return true;
        }
        return false;
    }

    public UserEntity createNewUser(UserEntity user){
        return userRepository.saveAndFlush(user);
    }

    public ResponseEntity<UserEntity> findUser(long id){
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
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