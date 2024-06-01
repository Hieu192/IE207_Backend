package com.thuctap.fastfood.repositories;

import com.thuctap.fastfood.entities.Product;
import com.thuctap.fastfood.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImagesRepository extends JpaRepository<ProductImage, Integer> {
    Optional<ProductImage> findProductImageByProductIdAndImageName(Product productId, String imageName);


}
