package de.oth.Packlon.repository;

import de.oth.Packlon.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);
}