package com.thuctap.fastfood.entities.embeddedId;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@Embeddable
public class CartProductKey implements Serializable {
  private Integer cartId;
  private Integer productId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CartProductKey that = (CartProductKey) o;
    return Objects.equals(cartId, that.cartId) && Objects.equals(productId, that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cartId, productId);
  }
}
