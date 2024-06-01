package com.thuctap.fastfood.entities.embeddedId;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ProductImportNoteDetailsId implements Serializable {
  private Integer importNoteId;
  private Integer productId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProductImportNoteDetailsId that = (ProductImportNoteDetailsId) o;
    return Objects.equals(importNoteId, that.importNoteId)
        && Objects.equals(productId, that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(importNoteId, productId);
  }
}
