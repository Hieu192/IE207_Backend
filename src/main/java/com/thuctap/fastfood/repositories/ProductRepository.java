package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p "+ "WHERE p.name LIKE %:query% ")
    List<Product> findByName(String q);
}
