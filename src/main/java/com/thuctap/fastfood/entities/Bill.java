package com.thuctap.fastfood.entities;

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
@Table(name = "bills")
@Entity(name = "Bill")
public class Bill {
  @Id
  @SequenceGenerator(name = "bill_sequence", sequenceName = "bill_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_sequence")
  @Column(name = "id", columnDefinition = "INT(11)")
  private Integer id;

  @Column(name = "status", columnDefinition = "VARCHAR(45)")
  private String status;

  @Column(name = "date_created", columnDefinition = "DATETIME")
  private LocalDateTime dateCreated;

  @Column(name = "date_successfully_paid", columnDefinition = "DATETIME")
  private LocalDateTime dateSuccessfullyPaid;

  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  @Column(name = "phone_number", columnDefinition = "VARCHAR(11)")
  private String phoneNumber;

  @Column(name = "notes", columnDefinition = "TEXT")
  private String notes;

  @Column(name = "payment_method", columnDefinition = " VARCHAR(255)")
  private String paymentMethod;

  @Column(name = "qr_payment_path", columnDefinition = "TEXT")
  private String qrPaymentPath;

  @Column(name = "total_price", columnDefinition = "DOUBLE")
  private Double totalPrice;

  @ManyToOne
  @JoinColumn(name = "id_user")
  private User user;

  @ManyToOne
  @JoinColumn(name = "id_staff")
  private Staff staff;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_export_note")
  private ProductExportNote exportNote;

  @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<BillDetail> billDetails = new HashSet<>();

  public void addBillDetail(BillDetail billDetail) {
    billDetails.add(billDetail);
  }
}
