package com.thuctap.fastfood.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Role")
@Table(name = "roles")
@PrimaryKeyJoinColumn(name = "id")
public class Role implements Serializable {
  @Id
  @SequenceGenerator(name = "role_sequence", sequenceName = "role_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence")
  @Column(name = "id", columnDefinition = "INT(11)")
  private Integer id;

  @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(45)")
  private String name;
}
