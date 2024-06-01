package com.thuctap.fastfood.dto;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
  private String userId;
  private String firstName;
  private String lastName;
  private String sex;
  private String address;
  private LocalDate dateOfBirth;
  private String email;
  private String phoneNumber;
}
