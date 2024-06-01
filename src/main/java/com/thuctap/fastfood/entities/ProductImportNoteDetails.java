package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thuctap.fastfood.entities.embeddedId.ProductImportNoteDetailsId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_import_note_details")
@Entity(name = "ProductImportNoteDetails")
public class ProductImportNoteDetails {
  @EmbeddedId private ProductImportNoteDetailsId primaryKey = new ProductImportNoteDetailsId();

  @ManyToOne
  @MapsId("importNoteId")
  @JoinColumn(name = "id_import_note")
  @JsonBackReference
  private ProductImportNote importNote;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "id_product")
  @JsonBackReference
  private Product product;

  private int quantity;

  private double price;
}
