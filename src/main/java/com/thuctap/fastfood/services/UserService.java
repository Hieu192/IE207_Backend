package com.thuctap.fastfood.services;

import com.thuctap.fastfood.dto.UserDTO;
import com.thuctap.fastfood.entities.User;
import com.thuctap.fastfood.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
  private final UserRepository userRepository;

  public Optional<User> findById(String idUser) {
    return userRepository.findById(idUser);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Optional<User> findByPhoneNumber(String phoneNumber) {
    return userRepository.findByPhoneNumber(phoneNumber);
  }

  public UserDTO toDTO(User user) {
    return UserDTO.builder()
            .userId(user.getId())
        .dateOfBirth(user.getDateOfBirth())
        .sex(String.valueOf(user.isSex()))
        .address(user.getAddress())
        .lastName(user.getLastName())
        .firstName(user.getFirstName())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .build();
  }

  public void changeFieldsDTOToEntity(UserDTO dto, User entity) {
    if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
    if (dto.getLastName() != null) entity.setLastName(dto.getLastName());
    if (dto.getAddress() != null) entity.setAddress(dto.getAddress());
    if (dto.getDateOfBirth() != null) entity.setDateOfBirth((dto.getDateOfBirth()));
    if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
    if (dto.getPhoneNumber() != null) entity.setPhoneNumber(dto.getPhoneNumber());
  }

  public User save(User user) {
    return userRepository.save(user);
  }
}
