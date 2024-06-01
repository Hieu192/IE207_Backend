package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Table(name = "staffs")
@Entity(name = "Staff")
public class Staff implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-id-generator")
  @GenericGenerator(
      name = "custom-id-generator",
      strategy = "com.thuctap.fastfood.entities.stringId.StaffIdGenerator")
  private String id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;

  @Column(name = "sex", nullable = false)
  private boolean sex;

  @Column(name = "address", nullable = false, unique = true)
  private String address;

  @Column(name = "is_working", nullable = false)
  private boolean isWorking = true;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @OneToMany(mappedBy = "staff")
  @JsonManagedReference
  private Set<ProductImportNote> importNotes = new HashSet<>();

  @OneToMany(mappedBy = "staff")
  private List<Bill> bills =  new ArrayList<>();
}
