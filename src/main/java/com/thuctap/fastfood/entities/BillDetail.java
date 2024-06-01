package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thuctap.fastfood.entities.embeddedId.BillDetailId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "bill_details")
@Entity(name = "BillDetail")
public class BillDetail {
  @EmbeddedId
  private BillDetailId primaryKey = new BillDetailId();

  @ManyToOne
  @MapsId("billId")
  @JoinColumn(name = "id_bill")
  @JsonBackReference
  private Bill bill;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "id_product")
  @JsonBackReference
  private Product product;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "price", columnDefinition = "DOUBLE")
  private double price;
}
