package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_images")
@Entity(name = "ProductImage")
public class ProductImage implements Serializable {
  @Id
  @SequenceGenerator(
      name = "product_images_sequence",
      sequenceName = "product_images_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_images_sequence")
  @Column(name = "id", columnDefinition = "INT(11)")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "id_product")
  @JsonBackReference
  private Product productId;

  @Column(name = "image_name", columnDefinition = "VARCHAR(255)")
  private String imageName;

  @Lob
  @Column(name = "image", columnDefinition = "LONGBLOB")
  private byte[] image;
}
