package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    List<Supplier> findAllByStatus(boolean status);
}
