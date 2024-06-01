package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Account;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
  Optional<Account> findByUsername(String username);
  Optional<Account> findAccountByIdPerson(String idPerson);

  List<Account> findByIdPersonStartingWith(String idPerson);
}
