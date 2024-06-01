package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "products")
@Entity(name = "Product")
public class Product implements Serializable {

  @Id
  @SequenceGenerator(
      name = "product_sequence",
      sequenceName = "product_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
  @Column(name = "id", columnDefinition = "INT(11)")
  private Integer id;

  @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
  private String name;

  @Column(name = "status")
  private Boolean status = true;

  @Column(name = "price")
  private Double price;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "stock")
  private Integer stock;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
  @JsonManagedReference
  private Set<ProductImage> images;

  @ManyToMany
  @JoinTable(
      name = "category_product",
      joinColumns = @JoinColumn(name = "id_product"),
      inverseJoinColumns = @JoinColumn(name = "id_category"))
  @JsonBackReference
  private Set<Category> categories = new HashSet<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  @JsonManagedReference
  Set<CartProduct> cartProducts = new HashSet<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  @JsonManagedReference
  Set<ProductImportNoteDetails> productImportNoteDetails = new HashSet<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  @JsonManagedReference
  Set<BillDetail> billDetails = new HashSet<>();



  public void addCategory(Category category) {
    categories.add(category);
  }

  public void addImage(ProductImage productImage) {
    images.add(productImage);
    productImage.setProductId(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Product product = (Product) o;

    return id.equals(product.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
