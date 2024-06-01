package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thuctap.fastfood.entities.embeddedId.CartProductKey;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart_product")
@Entity(name = "CartProduct")
public class CartProduct implements Serializable {
  @EmbeddedId private CartProductKey primaryKey = new CartProductKey();

  @ManyToOne
  @MapsId("cartId")
  @JoinColumn(name = "id_cart")
  @JsonBackReference
  private Cart cart;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "id_product")
  @JsonBackReference
  private Product product;

  private int quantity;
}
