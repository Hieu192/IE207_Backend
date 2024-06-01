package com.thuctap.fastfood.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierDTO {
    private Integer id;
    private String name;
    private boolean status;
}
