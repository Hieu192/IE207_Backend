package com.thuctap.fastfood.services;

import com.thuctap.fastfood.entities.Role;
import com.thuctap.fastfood.repositories.RoleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
  private final RoleRepository roleRepository;

  public Optional<Role> findById(Integer id) {
    return roleRepository.findById(id);
  }
  public Optional<Role> findByName(String name) {
    return roleRepository.findRoleByName(name);
  }
}
