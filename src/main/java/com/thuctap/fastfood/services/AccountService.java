package com.thuctap.fastfood.services;

import com.thuctap.fastfood.dto.AccountDTO;
import com.thuctap.fastfood.entities.Account;
import com.thuctap.fastfood.entities.Cart;
import com.thuctap.fastfood.entities.User;
import com.thuctap.fastfood.repositories.AccountRepository;
import com.thuctap.fastfood.repositories.CartRepository;
import com.thuctap.fastfood.repositories.RoleRepository;
import com.thuctap.fastfood.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final CartRepository cartRepository;
  private final RoleRepository roleRepository;

  public Account register(AccountDTO accountDTO) {
    User newUser = new User();
    Account newAccount = new Account();
    Cart newCart = new Cart();
    newUser.setEmail(accountDTO.getEmail());
    newUser.setPhoneNumber(accountDTO.getPhoneNumber());
    newAccount.setUsername(accountDTO.getUsername());
    newAccount.setPassword(accountDTO.getPassword());
    newAccount.setCart(newCart);
    cartRepository.save(newCart);
    User returnedUser = userRepository.save(newUser);
    newAccount.setIdPerson(returnedUser.getId());
    newAccount.setRole(roleRepository.findById(3).get());

    return accountRepository.save(newAccount);
  }

  public Optional<Account> findById(Integer id) {
    return accountRepository.findById(id);
  }

  public Optional<Account> findByUsername(String username) {
    return accountRepository.findByUsername(username);
  }

  public Account save(Account account) {
    return accountRepository.save(account);
  }

  public Optional<Account> findByIdPerson(String idPerson) {
    return accountRepository.findAccountByIdPerson(idPerson);
  }

  public AccountDTO toDTO(Account entity) {
    return AccountDTO.builder()
        .accountId(entity.getId())
        .username(entity.getUsername())
        .password(entity.getPassword())
        .status(entity.isStatus())
        .build();
  }

  public List<Account> findByIdPersonStartingWith(String idPerson) {
    return accountRepository.findByIdPersonStartingWith(idPerson);
  }
}
