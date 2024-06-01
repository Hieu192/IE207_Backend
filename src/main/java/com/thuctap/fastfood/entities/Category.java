package com.thuctap.fastfood.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "categories")
@Entity(name = "Category")
public class Category implements Serializable {

  @Id
  @SequenceGenerator(
      name = "categories_sequence",
      sequenceName = "categories_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_sequence")
  @Column(name = "id", columnDefinition = "INT(11)")
  private Integer id;

  @Column(name = "name", columnDefinition = "VARCHAR(255)")
  private String name;

  @Column(name = "description", columnDefinition = "VARCHAR(500)")
  private String description;

  @ManyToMany(mappedBy = "categories")
  @JsonManagedReference
  List<Product> products = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Category category = (Category) o;

    return id.equals(category.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
