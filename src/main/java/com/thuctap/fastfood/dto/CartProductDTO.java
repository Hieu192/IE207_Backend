package com.thuctap.fastfood.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductDTO {
  private Integer productId;
  private Integer quantity;
}
