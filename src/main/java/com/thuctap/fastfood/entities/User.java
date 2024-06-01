package com.thuctap.fastfood.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity(name = "User")
@Table(name = "users")
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-id-generator")
  @GenericGenerator(
      name = "custom-id-generator",
      strategy = "com.thuctap.fastfood.entities.stringId.UserIdGenerator")
  private String id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "sex")
  private boolean sex;

  @Column(name = "address", columnDefinition = "VARCHAR(1000)")
  private String address;

  @Column(name = "phone_number", nullable = false, unique = true)
  private String phoneNumber;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @OneToMany(mappedBy = "user")
  private List<Bill> bills = new ArrayList<>();
}
