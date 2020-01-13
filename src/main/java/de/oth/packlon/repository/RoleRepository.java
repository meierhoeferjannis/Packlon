package de.oth.packlon.repository;

import de.oth.packlon.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
}