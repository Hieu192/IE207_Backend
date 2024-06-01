package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
  List<Bill> findBillsByStatus(String status);
}
