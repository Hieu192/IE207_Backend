package com.thuctap.fastfood.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImportDTO {
  private Integer accountId;
  private Integer productId;
  private int quantity;
  private double price;
  private String supplier;
}
