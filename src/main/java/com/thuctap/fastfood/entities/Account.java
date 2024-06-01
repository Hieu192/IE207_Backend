package com.thuctap.fastfood.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Account")
@Table(name = "accounts")
public class Account implements Serializable {
  @Id
  @SequenceGenerator(
      name = "account_sequence",
      sequenceName = "account_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
  @Column(name = "id", columnDefinition = "INT(11)")
  private Integer id;

  @Column(name = "username", nullable = false, columnDefinition = "VARCHAR(255)")
  private String username;

  @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255 )")
  private String password;

  @Column(name = "status", nullable = false)
  private boolean status = true;

  @OneToOne
  @JoinColumn(name = "id_role")
  private Role role;

  @Column(name = "id_person", nullable = true, columnDefinition = "VARCHAR(10)")
  private String idPerson;

  @OneToOne
  @JoinColumn(name = "id_cart", referencedColumnName = "id")
  private Cart cart;
}
