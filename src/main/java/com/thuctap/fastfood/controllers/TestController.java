package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.entities.Account;
import com.thuctap.fastfood.entities.Cart;
import com.thuctap.fastfood.entities.Staff;
import com.thuctap.fastfood.entities.User;
import com.thuctap.fastfood.repositories.AccountRepository;
import com.thuctap.fastfood.repositories.CartRepository;
import com.thuctap.fastfood.repositories.RoleRepository;
import com.thuctap.fastfood.repositories.StaffRepository;
import com.thuctap.fastfood.repositories.UserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
  private final AccountRepository accountRepository;
  private final RoleRepository roleRepository;
  private final CartRepository cartRepository;
  private static UserRepository userRepository;
  private static StaffRepository staffRepository;

  public TestController(
      AccountRepository accountRepository,
      RoleRepository roleRepository,
      CartRepository cartRepository,
      UserRepository userRepository,
      StaffRepository staffRepository) {
    this.accountRepository = accountRepository;
    this.roleRepository = roleRepository;
    this.cartRepository = cartRepository;
    TestController.staffRepository = staffRepository;
    TestController.userRepository = userRepository;
  }

  public static List<User> listUsers() {
    return userRepository.findAll();
  }

  public static List<Staff> listStaffs() {
    return staffRepository.findAll();
  }

  @PostMapping
  public ResponseEntity<Account> saveAccount(@RequestBody Account account) {

    account.setRole(roleRepository.findById(3).get());
    log.info("account: ", account.toString());
    Cart cart = new Cart();
    account.setCart(cart);
    cartRepository.save(cart);
    account = accountRepository.save(account);
    log.info("account: ", account.toString());
    return ResponseEntity.ok(account);
  }

  @PostMapping("/user")
  public ResponseEntity<User> saveUser(@RequestBody User user) {
    return ResponseEntity.ok(userRepository.save(user));
  }
}
