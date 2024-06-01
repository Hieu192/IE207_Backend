package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Cart;
import com.thuctap.fastfood.entities.CartProduct;
import com.thuctap.fastfood.entities.Product;
import com.thuctap.fastfood.entities.embeddedId.CartProductKey;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, CartProductKey> {
  Optional<CartProduct> findCartProductByCartAndProduct(Cart cart, Product product);

  List<CartProduct> findCartProductsByCart(Cart cart);

  void deleteAllByCart(Cart cart);
}
