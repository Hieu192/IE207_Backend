package com.thuctap.fastfood.entities.embeddedId;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class BillDetailId implements Serializable {
  private Integer billId;
  private Integer productId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BillDetailId that = (BillDetailId) o;
    return Objects.equals(billId, that.billId) && Objects.equals(productId, that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(billId, productId);
  }
}
