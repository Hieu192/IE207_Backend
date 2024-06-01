package com.thuctap.fastfood.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "good_export_notes")
@Entity(name = "ProductExportNote")
public class ProductExportNote {
  @Id

      @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @OneToOne
  @JoinColumn(name = "id_bill")
  private Bill bill;

  @Column(name = "date", columnDefinition = "DATETIME")
  private LocalDateTime date;
}
