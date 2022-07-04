package com.example.demo.models.repos;

import com.example.demo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role, Long> {

    Role findByName(String role_admin);
}
