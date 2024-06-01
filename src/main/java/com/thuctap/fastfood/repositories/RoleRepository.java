package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findRoleByName(String name);
}
