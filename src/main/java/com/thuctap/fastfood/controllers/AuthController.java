package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.FastfoodApplication;
import com.thuctap.fastfood.dto.AccountDTO;
import com.thuctap.fastfood.entities.Account;
import com.thuctap.fastfood.entities.Role;
import com.thuctap.fastfood.entities.Staff;
import com.thuctap.fastfood.entities.User;
import com.thuctap.fastfood.services.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserService userService;
  private final AccountService accountService;
  private final RoleService roleService;
  private final StaffService staffService;
  private final EmailSenderService senderService;

  @GetMapping("/role")
  public ResponseEntity<Role> getRoleOfAccount(@RequestParam Integer accountId) {
    Optional<Account> accountOptional = accountService.findById(accountId);
    return accountOptional
        .map(account -> ResponseEntity.ok(account.getRole()))
        .orElseGet(() -> ResponseEntity.ok(null));
  }

  @PostMapping("/login")
  public ResponseEntity<AccountDTO> login(@NonNull @RequestBody AccountDTO account) {
    Optional<Account> accountOptional = accountService.findByUsername(account.getUsername());
    if (accountOptional.isPresent()) {
      Account dbAccount = accountOptional.get();
      if (account.getPassword().equals(dbAccount.getPassword())) {
        AccountDTO dto = accountService.toDTO(dbAccount);
        return ResponseEntity.ok(dto);
      }
    }
    return ResponseEntity.ok().body(null);
  }

  @PostMapping("/register")
  public ResponseEntity<Map<String, Object>> register(@NonNull @RequestBody AccountDTO accountDTO) {
    Map<String, Object> map = new HashMap<>();
    boolean isValid = true;
    Optional<Account> accountOptional = accountService.findByUsername(accountDTO.getUsername());
    if (accountOptional.isPresent()) {
      map.put("username", "Tên tài khoản đã tồn tại");
      isValid = false;
    }
    Optional<User> userOptional = userService.findByEmail(accountDTO.getEmail());
    if (userOptional.isPresent()) {
      map.put("email", "Email đã tồn tài trên tài khoản khác");
      isValid = false;
    }

    userOptional = userService.findByPhoneNumber(accountDTO.getPhoneNumber());
    if (userOptional.isPresent()) {
      map.put("phoneNumber", "Số điện thoại đã tồn tại trên tài khoản khác");
      isValid = false;
    }

    if (!isValid) {
      map.put("idAccount", null);
    }
    if (isValid) {
      Account registeredAccount = accountService.register(accountDTO);
      map.put("idAccount", registeredAccount.getId());
    }

    return ResponseEntity.ok(map);
  }

  @GetMapping("/checkUserRegisterRecently")
  public ResponseEntity<Boolean> checkRegisterRecently(
      @RequestParam("idAccount") Integer idAccount) {
    Optional<Account> accountOptional = accountService.findById(idAccount);
    if (accountOptional.isPresent()) {
      Account returnedAccount = accountOptional.get();
      // TODO: Lỡ account hiện tại là nhân viên hay người dùng
      Role role = returnedAccount.getRole();
      if (role.getName().equals("USER")) {
        System.out.println("test");
        Optional<User> returnedUser = userService.findById(returnedAccount.getIdPerson());

        if (returnedUser.isPresent()) {
          System.out.println("test");
          User user = returnedUser.get();
          if (user.getFirstName() == null) {
            return ResponseEntity.ok(true);
          }
          return ResponseEntity.ok(false);
        }
      } else {
        return ResponseEntity.ok(false);
      }
    }
    return ResponseEntity.ok(false);
  }

  @PostMapping("/change-password")
  public ResponseEntity<Map<String, String>> changePassword(
      @RequestBody AccountDTO accountDTO, @RequestParam("newPassword") String newPassword) {
    Map<String, String> map = new HashMap<>();
    Optional<Account> accountOptional = accountService.findById(accountDTO.getAccountId());
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      if (!account.getPassword().equals(accountDTO.getPassword())) {
        map.put("error", "Mật khẩu cũ không đúng");
      } else {
        account.setPassword(newPassword);
        String savedPassword = accountService.save(account).getPassword();
        if (!savedPassword.equals(newPassword)) {
          map.put("error", "Thay đổi mật khẩu thất bại");
        }
      }
    }
    return ResponseEntity.ok(map);
  }

  @PostMapping("/change-status")
  public ResponseEntity<Boolean> changeStatus(@RequestParam("accountId") Integer accountId) {
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      account.setStatus(!account.isStatus());
      accountService.save(account);
      return ResponseEntity.ok(true);
    }
    return ResponseEntity.ok(false);
  }

  @GetMapping("/check-email")
  public ResponseEntity<Integer> checkEmail(
      @RequestParam("email") String email, @RequestParam("isCreateOTP") Boolean isCreateOTP) {
    Optional<User> userOptional = userService.findByEmail(email);
    Optional<Staff> staffOptional = staffService.findByEmail(email);
    if (userOptional.isPresent()) {
      Account account = accountService.findByIdPerson(userOptional.get().getId()).get();
      if (isCreateOTP) {
        double otpNumber = Math.random() * 100000;
        String otp = String.valueOf((int) otpNumber);
        FastfoodApplication.forgotPasswordMap.put(email, otp);
        senderService.sendEmail(
            email, "Mã OTP quên mật khẩu", "OTP để thay đổi mật khẩu mới của bạn là: " + otp);
      }
      return ResponseEntity.ok(account.getId());
    }
    if (staffOptional.isPresent()) {
      Account account = accountService.findByIdPerson(staffOptional.get().getId()).get();
      if (isCreateOTP) {
        double otpNumber = Math.random() * 100000;
        String otp = String.valueOf((int) otpNumber);
        FastfoodApplication.forgotPasswordMap.put(email, otp);
        senderService.sendEmail(
            email, "Mã OTP quên mật khẩu", "OTP để thay đổi mật khẩu mới cho tài khoản + " + account.getUsername() +" là: " + otp);
      }
      return ResponseEntity.ok(account.getId());
    }
    return ResponseEntity.ok(null);
  }

  @GetMapping("/check-email-and-otp")
  public ResponseEntity<Boolean> checkEmailAndOTP(
      @RequestParam("email") String email, @RequestParam("otp") String otp) {
    if (FastfoodApplication.forgotPasswordMap.containsKey(email)) {
      String otpInMap = FastfoodApplication.forgotPasswordMap.get(email);
      if (otpInMap.equals(otp)) {
        return ResponseEntity.ok(true);
      }
    }
    return ResponseEntity.ok(false);
  }

  @GetMapping("/check-username-email-phone-number")
  public ResponseEntity<Map<String, String>> checkUsername(
      @RequestParam("username") String username,
      @RequestParam("email") String email,
      @RequestParam("phoneNumber") String phoneNumber) {
    Map<String, String> map = new HashMap<>();
    Optional<Account> accountOptional = accountService.findByUsername(username);
    if (accountOptional.isPresent()) {
      map.put("username", "Username đã tồn tại");
    } else {
      map.put("username", "");
    }

    Optional<User> userOptional = userService.findByEmail(email);
    Optional<Staff> staffOptional = staffService.findByEmail(email);
    if (userOptional.isPresent() || staffOptional.isPresent()) {
      map.put("email", "Email đã tồn tại");
    } else {
      map.put("email", "");
    }
    userOptional = userService.findByPhoneNumber(phoneNumber);
    staffOptional = staffService.findByPhoneNumber(phoneNumber);
    if (userOptional.isPresent() || staffOptional.isPresent())
      map.put("phoneNumber", "Số điện thoại đã tồn tại");
    else map.put("phoneNumber", "");

    return ResponseEntity.ok(map);
  }

  @PostMapping("/change-password-no-old-password")
  public ResponseEntity<Map<String, String>> changePasswordNoOldPassword(
      @RequestParam Integer accountId, @RequestParam String newPassword) {
    Map<String, String> returnedMap = new HashMap<>();
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      account.setPassword(newPassword);
      accountService.save(account);
    } else returnedMap.put("error", "Id tài khoản không tồn tại");
    return ResponseEntity.ok(returnedMap);
  }
}
