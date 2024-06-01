package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Category;
import com.thuctap.fastfood.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String q);
}
