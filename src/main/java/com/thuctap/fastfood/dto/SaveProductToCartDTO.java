package com.thuctap.fastfood.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveProductToCartDTO {
  private Integer accountId;
  private List<CartProductDTO> cartProductDTOS;
}
