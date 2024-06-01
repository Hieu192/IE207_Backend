package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Table(name = "suppliers")
@Entity(name = "Supplier")
public class Supplier {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "status")
  private boolean status = true;

  @OneToMany(mappedBy = "supplier")
  @JsonManagedReference
  Set<ProductImportNote> importNotes = new HashSet<>();
}
