package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p "+ "WHERE p.name LIKE %:search% ")
    List<Product> findByName(@Param("search") String search);
}
