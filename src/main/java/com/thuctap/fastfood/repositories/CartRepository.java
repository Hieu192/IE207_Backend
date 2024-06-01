package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Cart;
import com.thuctap.fastfood.entities.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
