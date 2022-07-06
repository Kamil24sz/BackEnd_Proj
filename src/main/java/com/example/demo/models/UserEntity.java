package com.example.demo.models;

import com.example.demo.requests.UserRegisterRequest;
import com.example.demo.security.SetupDataLoader;
import lombok.*;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.security.SecurityConfiguration;

import javax.persistence.*;
import java.util.*;

@Table(name = "USER_ENTITY")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private boolean enabled;
    private boolean tokenExpired;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "User_Entity_id"),
            inverseJoinColumns = @JoinColumn(name = "Role_id")
    )
    private Collection<Role> roles;

    @OneToMany
    private final List<TicketEntity> ticket = new ArrayList<>();

}