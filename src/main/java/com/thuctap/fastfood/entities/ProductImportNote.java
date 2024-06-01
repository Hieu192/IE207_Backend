package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_import_notes")
@Entity(name = "ProductImportNote")
public class ProductImportNote {

  @Id
  @SequenceGenerator(
      name = "product_import_notes_sequence",
      sequenceName = "product_import_notes_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_import_notes_sequence")
  @Column(name = "id", columnDefinition = "INT(11)")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "id_staff", columnDefinition = "VARCHAR(10)")
  @JsonBackReference
  private Staff staff;

  @Column(name = "date")
  private LocalDateTime date;

  @OneToMany(mappedBy = "importNote", cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<ProductImportNoteDetails> productImportNoteDetails = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "id_supplier")
  @JsonBackReference
  private Supplier supplier;

  public void addProductImportNoteDetails(ProductImportNoteDetails importNoteDetails) {
    productImportNoteDetails.add(importNoteDetails);
  }
}
