package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.dto.AccountDTO;
import com.thuctap.fastfood.dto.UserDTO;
import com.thuctap.fastfood.entities.Account;
import com.thuctap.fastfood.entities.Staff;
import com.thuctap.fastfood.entities.User;
import com.thuctap.fastfood.services.AccountService;
import com.thuctap.fastfood.services.StaffService;
import com.thuctap.fastfood.services.UserService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final AccountService accountService;
  private final UserService userService;
  private final StaffService staffService;

  @GetMapping("/find-by-id")
  public ResponseEntity<UserDTO> findStaffById(@RequestParam("userId") String userId) {
    Optional<User> userOptional = userService.findById(userId);
    if (userOptional.isPresent()) {
      User user= userOptional.get();
      UserDTO dto = userService.toDTO(user);
      return ResponseEntity.ok(dto);
    }
    return ResponseEntity.ok(null);
  }

  @GetMapping("/find-account")
  public ResponseEntity<AccountDTO> findAccount(@RequestParam("userId") String userId) {
    Optional<User> userOptional = userService.findById(userId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      Optional<Account> accountOptional = accountService.findByIdPerson(user.getId());
      if (accountOptional.isPresent()) {
        Account account = accountOptional.get();
        AccountDTO dto = accountService.toDTO(account);
        return ResponseEntity.ok(dto);
      }
    }
    return ResponseEntity.ok(null);
  }

  @GetMapping("/find-user-by-account-id")
  public ResponseEntity<UserDTO> findUserByAccountId(@RequestParam("accountId") Integer accountId) {
    UserDTO userDTO = null;
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Optional<User> userOptional = userService.findById(accountOptional.get().getIdPerson());
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        userDTO = userService.toDTO(user);
      }
    }
    return ResponseEntity.ok(userDTO);
  }

  @PostMapping("/update-info-by-account")
   public ResponseEntity<Boolean> updateInfoByAccount(@RequestBody UserDTO userDTO, @RequestParam("accountId") Integer accountId) {
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Optional<User> userOptional  = userService.findById(accountOptional.get().getIdPerson());
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        userService.changeFieldsDTOToEntity(userDTO, user);
        userService.save(user);
        return ResponseEntity.ok(true);
      }
    }
    return ResponseEntity.ok(false);
  }

  @GetMapping("/check-email-phone-number")
  public ResponseEntity<Map<String, String>> checkEmailAndPhoneNumber(
      @RequestParam("email") String email,
      @RequestParam("phoneNumber") String phoneNumber,
      @RequestParam("accountId") Integer accountId) {
    Map<String, String> map = new HashMap<>();
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      Optional<User> userOfAccountOptional = userService.findById(account.getIdPerson());
      if (userOfAccountOptional.isPresent()) {
        User userOfAccount = userOfAccountOptional.get();

        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
          User user = userOptional.get();
          if (!user.getId().equals(userOfAccount.getId()))
            map.put("email", "Đã tồn tại tài khoản có email này");
        }
        userOptional = userService.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
          User user = userOptional.get();
          if (!user.getId().equals(userOfAccount.getId()))
            map.put("phoneNumber", "Đã tồn tại tài khoản có số điện thoại này");
        }

        Optional<Staff> staffOptional = staffService.findByEmail(email);
        if (staffOptional.isPresent()) {
          map.put("email", "Đã tồn tại tài khoản có email này");
        }

        staffOptional = staffService.findByPhoneNumber(phoneNumber);
        if (staffOptional.isPresent()) {
          map.put("phoneNumber", "Đã tồn tại tài khoản có số điện thoại này");
        }
      }
    }
    return ResponseEntity.ok(map);
  }

  @PostMapping("/updateInformation")
  public ResponseEntity<Boolean> updateInformation(@RequestBody Map<String, Object> map) {
    try {
      Integer idAccount = (Integer) map.get("idAccount");
      LinkedHashMap<String, String> linkedHashMapUserInfo =
          (LinkedHashMap<String, String>) map.get("userInfo");
      UserDTO userDTO = new UserDTO();
      if (linkedHashMapUserInfo.containsKey("lastName"))
        userDTO.setLastName(linkedHashMapUserInfo.get("lastName"));
      if (linkedHashMapUserInfo.containsKey("firstName"))
        userDTO.setFirstName(linkedHashMapUserInfo.get("firstName"));
      if (linkedHashMapUserInfo.containsKey("dateOfBirth"))
        userDTO.setDateOfBirth(LocalDate.parse(linkedHashMapUserInfo.get("dateOfBirth")));
      if (linkedHashMapUserInfo.containsKey("sex")) {
        String sex = linkedHashMapUserInfo.get("sex");
        userDTO.setSex(sex);
      }
      if (linkedHashMapUserInfo.containsKey("address"))
        userDTO.setAddress(linkedHashMapUserInfo.get("address"));
      if (linkedHashMapUserInfo.containsKey("email"))
        userDTO.setEmail(linkedHashMapUserInfo.get("email"));
      if (linkedHashMapUserInfo.containsKey("phoneNumber"))
        userDTO.setPhoneNumber(linkedHashMapUserInfo.get("phoneNumber"));
      Optional<Account> accountOptional = accountService.findById(idAccount);
      if (accountOptional.isPresent()) {
        Account account = accountOptional.get();
        Optional<User> userOptional = userService.findById(account.getIdPerson());
        userOptional.ifPresent(
            user -> {
              if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());

              if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
              if (userDTO.getSex() != null) {
                user.setSex(userDTO.getSex().equals("1"));
              }
              if (userDTO.getDateOfBirth() != null) user.setDateOfBirth(userDTO.getDateOfBirth());
              if (userDTO.getAddress() != null) user.setAddress(userDTO.getAddress());
              if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
              if (userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
              userService.save(user);
            });
      }
    } catch (Exception ex) {
      return ResponseEntity.ok(false);
    }
    return ResponseEntity.ok(true);
  }
}
