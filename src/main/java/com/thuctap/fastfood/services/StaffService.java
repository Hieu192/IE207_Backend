package com.thuctap.fastfood.services;

import com.thuctap.fastfood.dto.StaffDTO;
import com.thuctap.fastfood.entities.Account;
import com.thuctap.fastfood.entities.Staff;
import com.thuctap.fastfood.repositories.AccountRepository;
import com.thuctap.fastfood.repositories.StaffRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class StaffService {
  private final StaffRepository staffRepository;
  private final AccountRepository accountRepository;

  public Optional<Staff> findById(String id) {
    return staffRepository.findById(id);
  }

  public Optional<Staff> findByPhoneNumber(String phoneNumber) {
    return staffRepository.findStaffByPhoneNumber(phoneNumber);
  }

  public Optional<Staff> findByEmail(String email) {

    return staffRepository.findStaffByEmail(email);
  }

  public List<Staff> findAllIsWorking() {
    return staffRepository.findAll().stream()
        .filter(staff -> {
          if (!staff.isWorking())
              return false;
          Account account = accountRepository.findAccountByIdPerson(staff.getId()).get();
          return account.getRole().getName().equals("STAFF");
        })
        .collect(Collectors.toList());
  }

    public List<Staff> findAllAdminsIsWorking() {
        return staffRepository.findAll().stream()
                .filter(staff -> {
                    if (!staff.isWorking())
                        return false;
                    Account account = accountRepository.findAccountByIdPerson(staff.getId()).get();
                    return account.getRole().getName().equals("ADMIN");
                })
                .collect(Collectors.toList());
    }

  public List<Staff> findAllNotWorking() {

    return staffRepository.findAll().stream()
        .filter(staff -> {
          if (staff.isWorking())
            return false;
          Account account = accountRepository.findAccountByIdPerson(staff.getId()).get();
          return account.getRole().getName().equals("STAFF");
        })
        .collect(Collectors.toList());
  }

    public List<Staff> findAllAdminsNotWorking() {

        return staffRepository.findAll().stream()
                .filter(staff -> {
                    if (staff.isWorking())
                        return false;
                    Account account = accountRepository.findAccountByIdPerson(staff.getId()).get();
                    return account.getRole().getName().equals("ADMIN");
                })
                .collect(Collectors.toList());
    }

  public StaffDTO toDTO(Staff staff) {
    return StaffDTO.builder()
        .id(staff.getId())
        .firstName(staff.getFirstName())
        .lastName(staff.getLastName())
        .dateOfBirth(staff.getDateOfBirth())
        .sex(staff.isSex())
        .phoneNumber(staff.getPhoneNumber())
        .address(staff.getAddress())
        .isWorking(staff.isWorking())
        .email(staff.getEmail())
        .build();
  }

  public void changeFieldsDTOToEntity(StaffDTO dto, Staff entity) {
    if (dto.getFirstName() != null) entity.setFirstName(dto.getFirstName());
    if (dto.getLastName() != null) entity.setLastName(dto.getLastName());
    if (dto.getAddress() != null) entity.setAddress(dto.getAddress());
    if (dto.getDateOfBirth() != null) entity.setDateOfBirth((dto.getDateOfBirth()));
    if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
    if (dto.getPhoneNumber() != null) entity.setPhoneNumber(dto.getPhoneNumber());
  }

  public Staff save(Staff staff) {
    return staffRepository.save(staff);
  }
}
