package com.thuctap.fastfood.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffDTO {
  private String id;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private Boolean sex;
  private String phoneNumber;
  private String address;
  private Boolean isWorking;
  private String email;
}
