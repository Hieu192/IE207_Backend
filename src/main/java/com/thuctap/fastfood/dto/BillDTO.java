package com.thuctap.fastfood.dto;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
  private Integer billId;
  private Integer accountId;
  private List<CartProductDTO> cart;
  private String notes;
  private String address;
  private String phoneNumber;
  private Double totalPrice;
  private String paymentMethod;
  private String status;
  private LocalDateTime dateCreated;
  private LocalDateTime dateSuccessfullyPaid;
  private String qrPaymentPath;
}
