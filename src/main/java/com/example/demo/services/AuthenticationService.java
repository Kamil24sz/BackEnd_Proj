package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.models.repos.RoleRepository;
import com.example.demo.models.repos.UserRepository;
import com.example.demo.requests.UserLoginRequest;
import com.example.demo.requests.UserRegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AuthenticationService implements UserDetailsService {

    private final MailService mailService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final HttpServletRequest httpServletRequest;


    public  boolean checkUser(String email){
        HttpSession session = httpServletRequest.getSession();
        Long creatorID = (Long)session.getAttribute(email);
        if(creatorID == null){
            return false;
        }
        UserEntity creator = userRepository.findById(creatorID).orElse(null);
        if(creator == null){
            return false;
        }
        return true;
    }

    public boolean registerUser(UserRegisterRequest userRegisterRequest) throws MessagingException {

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(userRegisterRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(jsonString,headers);
            ResponseEntity<String> response =  new RestTemplate().postForEntity("http://localhost:8080/api/user/create", entity, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }




        this.mailService.sendEmail("kamil240sz@gmail.com","Registration", "Welcome on my web site, please feel welcome");

        return true;
    }

    public boolean loginUser(HttpServletRequest httpServletRequest, UserLoginRequest userLoginRequest){
        HttpSession session = httpServletRequest.getSession();

        if(userService.login(userLoginRequest))
        {
            UserEntity user = userRepository.findFirstByEmail(userLoginRequest.getEmail());
            session.setAttribute(userLoginRequest.getEmail(), user.getId()); //root id
            return true;
        }

        return false;
    }

    public boolean logoutUser(HttpServletRequest httpServletRequest, String email){
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute(email) == null){
            return false;
        }
        session.removeAttribute(email); //root id
        return true;
    }

    public boolean checkIfUserExists(String email){
        return userService.checkIfUserExists(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        UserEntity user = userRepository.findFirstByEmail(email);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Arrays.asList(
                            roleRepository.findByName("ROLE_USER"))));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.isEnabled(), true, true,
                true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    public boolean promoteUser(long id) {


        UserEntity promotedUser = userRepository.findById(id).orElse(null);
        if(promotedUser.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_ADMIN")))
            return false;

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        List<Role> list = new ArrayList<Role>();
        list.add(adminRole);

        promotedUser.setRoles(list);
        userRepository.save(promotedUser);

        return true;
    }

    public boolean degradeUser(long id) {

        UserEntity degradedUser = userRepository.findById(id).orElse(null);
        if(degradedUser.getRoles().stream().anyMatch(a -> a.getName().equals("ROLE_USER")))
            return false;

        Role userRole = roleRepository.findByName("ROLE_USER");

        List<Role> list = new ArrayList<Role>();
        list.add(userRole);

        degradedUser.setRoles(list);
        userRepository.save(degradedUser);

        return true;
    }

    public boolean deleteUser(long id) {

        UserEntity deletedUser = userRepository.findById(id).orElse(null);

        userRepository.delete(deletedUser);

        return true;
    }
}
