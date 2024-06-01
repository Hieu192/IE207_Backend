package com.thuctap.fastfood.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
  private Integer id;
  private String name;
  private String price;
  private String description;
  private Boolean status;
  private Set<String> categories = new HashSet<>();
}
