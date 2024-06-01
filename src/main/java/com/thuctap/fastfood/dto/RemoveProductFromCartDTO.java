package com.thuctap.fastfood.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveProductFromCartDTO {
  private Integer accountId;
  private Integer productId;
}
