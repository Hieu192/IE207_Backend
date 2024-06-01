package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, String> {
  Optional<Staff> findStaffByEmail(String email);
  Optional<Staff> findStaffByPhoneNumber(String phoneNumber);
}
