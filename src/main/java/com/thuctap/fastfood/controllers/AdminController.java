package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.dto.AccountDTO;
import com.thuctap.fastfood.dto.StaffDTO;
import com.thuctap.fastfood.dto.UserDTO;
import com.thuctap.fastfood.entities.Account;
import com.thuctap.fastfood.entities.Role;
import com.thuctap.fastfood.entities.Staff;
import com.thuctap.fastfood.entities.User;
import com.thuctap.fastfood.services.AccountService;
import com.thuctap.fastfood.services.RoleService;
import com.thuctap.fastfood.services.StaffService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.thuctap.fastfood.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admins")
public class AdminController {
  private final StaffService staffService;
  private final AccountService accountService;
  private final RoleService roleService;
  private final UserService userService;


  @GetMapping("/users")
  public ResponseEntity<List<UserDTO>> findAllUsers(@RequestParam("isActive") Boolean isActive) {
    List<UserDTO> userDTOS = new ArrayList<>();

    List<Account> accounts = accountService.findByIdPersonStartingWith("KH");
    accounts.forEach(account -> {
      if (account.isStatus() == isActive) {
        User user = userService.findById(account.getIdPerson()).get();
        UserDTO dto = userService.toDTO(user);
        userDTOS.add(dto);
      }

    });
    return ResponseEntity.ok(userDTOS);
  }
  @GetMapping("/staffs")
  public ResponseEntity<List<StaffDTO>> findAllStaff(@RequestParam("isWorking") Boolean isWorking) {
    List<StaffDTO> staffDTOS = new ArrayList<>();
    if (isWorking) {
      staffService
          .findAllIsWorking()
          .forEach(
              staff -> {
                StaffDTO dto = staffService.toDTO(staff);
                staffDTOS.add(dto);
              });

    } else {
      staffService
          .findAllNotWorking()
          .forEach(
              staff -> {
                StaffDTO dto = staffService.toDTO(staff);
                staffDTOS.add(dto);
              });
    }
    return ResponseEntity.ok(staffDTOS);
  }

  @GetMapping
  public ResponseEntity<List<StaffDTO>> findAllAdmins(@RequestParam("isWorking") Boolean isWorking) {
    List<StaffDTO> staffDTOS = new ArrayList<>();
    if (isWorking) {
      staffService
              .findAllAdminsIsWorking()
              .forEach(
                      staff -> {
                        StaffDTO dto = staffService.toDTO(staff);
                        staffDTOS.add(dto);
                      });

    } else {
      staffService
              .findAllAdminsNotWorking()
              .forEach(
                      staff -> {
                        StaffDTO dto = staffService.toDTO(staff);
                        staffDTOS.add(dto);
                      });
    }
    return ResponseEntity.ok(staffDTOS);
  }



  @PostMapping("/change-staff-status")
  public ResponseEntity<Boolean> quit(@RequestParam("staffId") String staffId) {
    Optional<Staff> staffOptional = staffService.findById(staffId);
    if (staffOptional.isPresent()) {
      Staff staff = staffOptional.get();
      staff.setWorking(!staff.isWorking());
      Account account = accountService.findByIdPerson(staff.getId()).get();
      account.setStatus(!account.isStatus());

      staffService.save(staff);
      accountService.save(account);
      return ResponseEntity.ok(true);
    }
    return ResponseEntity.ok(false);
  }

  @PostMapping("/create-staff")
  public ResponseEntity<String> createStaff(@RequestBody AccountDTO accountDTO, @RequestParam("role") String role) {
    Staff staff = new Staff();
    staff.setEmail(accountDTO.getEmail());
    staff.setPhoneNumber(accountDTO.getPhoneNumber());
    staff.setAddress(accountDTO.getAddress());
    staff.setSex(accountDTO.getSex());
    staff.setFirstName(accountDTO.getFirstName());
    staff.setLastName(accountDTO.getLastName());
    staff.setDateOfBirth(accountDTO.getDateOfBirth());
    staff = staffService.save(staff);
    if (staff.getId() == null) return ResponseEntity.ok(null);
    Account account = new Account();
    Optional<Role> roleOptional;
    if (role.equals("STAFF")) {
      roleOptional = roleService.findByName("STAFF");
    }else {
      roleOptional = roleService.findByName("ADMIN");
    }
    roleOptional.ifPresent(account::setRole);
    account.setUsername(accountDTO.getUsername());
    account.setPassword(accountDTO.getPassword());
    account.setStatus(true);
    account.setIdPerson(staff.getId());
    account = accountService.save(account);
    if (account.getId() == null) return ResponseEntity.ok(null);
    return ResponseEntity.ok(staff.getId());
  }


}
