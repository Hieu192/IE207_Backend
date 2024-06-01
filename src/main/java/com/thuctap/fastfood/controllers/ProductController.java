package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.dto.ProductDTO;
import com.thuctap.fastfood.entities.Category;
import com.thuctap.fastfood.entities.Product;
import com.thuctap.fastfood.entities.ProductImage;
import com.thuctap.fastfood.services.CategoryService;
import com.thuctap.fastfood.services.ProductService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService productService;
  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<Integer> saveProduct(@RequestBody ProductDTO productDTO) {
    // add new
    Product product = productService.convertProductDTOToProduct(productDTO);
    product.setStock(0);
    product = productService.saveProduct(product);
    return ResponseEntity.ok(product.getId());
  }

  @PostMapping("/update")
  public ResponseEntity<Integer> updateProduct(@RequestBody ProductDTO productDTO) {
    Optional<Product> productOptional = productService.findById(productDTO.getId());
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      product.setName(productDTO.getName());
      product.setPrice(Double.valueOf(productDTO.getPrice()));
      product.setDescription(productDTO.getDescription());
      product.getCategories().clear();
      for (String idCate : productDTO.getCategories()) {
        Category category = categoryService.findById(Integer.parseInt(idCate)).get();
        product.getCategories().add(category);
      }
      product = productService.saveProduct(product);
      return ResponseEntity.ok(product.getId());
    }
    return ResponseEntity.ok(null);
  }
  @PostMapping("/delete")
  public ResponseEntity<String> deleteProduct(@RequestParam("productId") Integer productId) {
    Optional<Product> productOptional = productService.findById(productId);
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      if (product.getStock() > 0) {
        return ResponseEntity.ok("Hàng đã được lập phiếu nhập, không thể xóa");
      }

      if (!product.getBillDetails().isEmpty()) {
        return ResponseEntity.ok("Sản phẩm đã có trong hóa đơn, không thể xóa");
      }

      product.getCartProducts().clear();
      product.getCategories().clear();
      product =productService.saveProduct(product);
      productService.deleteProduct(product);
      return ResponseEntity.ok("");
    }
    return ResponseEntity.ok("Mã sản phầm không tồn tại");
  }

  @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Boolean> uploadImage(
      @RequestParam MultipartFile file, @RequestParam("productId") Integer productId)
      throws IOException {
    ProductImage productImage = new ProductImage();
    productImage.setImage(file.getBytes());
    productImage.setImageName("anh 1");
    Optional<Product> productOptional = productService.findById(productId);
    productOptional.ifPresent(
        product -> {
          productService.saveImage(productImage, product);
        });
    return ResponseEntity.ok(true);
  }

  @PostMapping(value = "/updateImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Boolean> updateImage(
      @RequestParam MultipartFile file, @RequestParam("productId") Integer productId)
      throws IOException {
    Optional<Product> productOptional = productService.findById(productId);
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      Optional<ProductImage> productImageOptional =
          productService.findImageByProductAndName(product, "anh 1");
      if (productImageOptional.isPresent()) {
        ProductImage productImage = productImageOptional.get();
        productImage.setImage(file.getBytes());
        productService.saveImage(productImage);
        return ResponseEntity.ok(true);
      }
    }
    return ResponseEntity.ok(false);
  }

  @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<byte[]> getImage(
      @RequestParam Integer productId, @RequestParam String imageName) {
    Optional<Product> productOptional = productService.findById(productId);
    if (productOptional.isPresent()) {
      Optional<ProductImage> productImageOptional =
          productService.findImageByProductAndName(productOptional.get(), imageName);
      if (productImageOptional.isPresent()) {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(productImageOptional.get().getImage());
      }
    }
    return null;
  }

  @GetMapping
  public ResponseEntity<List<Product>> findAllProducts() {
    List<Product> products =
        productService.findAll().stream()
            .filter(product -> product.getStatus() && product.getStock() > 0)
            .map(
                product -> {
                  product.setImages(new HashSet<>());
                  return product;
                })
            .collect(Collectors.toList());
    return ResponseEntity.ok(products);
  }

  @GetMapping("/allProducts")
  public ResponseEntity<List<Product>> findAllProductsAdminStaff() {
    List<Product> products =
        productService.findAll().stream()
            .map(
                product -> {
                  product.setImages(new HashSet<>());
                  return product;
                })
            .collect(Collectors.toList());
    return ResponseEntity.ok(products);
  }


  @GetMapping("/{productId}")
  public ResponseEntity<ProductDTO> findById(@PathVariable Integer productId) {
    Optional<Product> productOptional = productService.findById(productId);
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      ProductDTO productDTO =
          ProductDTO.builder()
              .id(product.getId())
              .name(product.getName())
              .price(String.valueOf(product.getPrice()))
              .description(product.getDescription())
              .categories(
                  product.getCategories().stream()
                      .map(
                          category -> {
                            return String.valueOf(category.getId());
                          })
                      .collect(Collectors.toSet()))
              .build();
      return ResponseEntity.ok(productDTO);
    }
    return ResponseEntity.ok(null);
  }

  @GetMapping("/search")
  public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String search) {
    List<Product> products = new ArrayList<>();
    if (search != null || !search.isEmpty()) {
      search = search.replace("+", " ");
      products = productService.searchProduct(search);
    }
      return ResponseEntity.ok(products);
  }

  @GetMapping("/category")
  public ResponseEntity<List<Product>> findByCategory(@RequestParam String category) {
    category = category.replace("+", " ");
    List<Product> products = productService.findByCategory(category);

    return ResponseEntity.ok(products);
  }
  @GetMapping("/filter")
  public ResponseEntity<List<Product>> findByfilter(@RequestParam String category, @RequestParam Double minPrice, @RequestParam Double maxPrice, @RequestParam String sort) {
    try {
      // Giải mã tham số category
      category = URLDecoder.decode(category, "UTF-8");
      sort = URLDecoder.decode(sort, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // Xử lý lỗi giải mã nếu cần
      e.printStackTrace();
    }
    List<Product> products = new ArrayList<>();
    if (category == null || category.isEmpty()) {
      products = productService.findAll().stream()
              .filter(product -> product.getStatus() && product.getStock() > 0)
              .map(
                      product -> {
                        product.setImages(new HashSet<>());
                        return product;
                      })
              .collect(Collectors.toList());;
    } else {
      category = category.replace("+", " ");
      products = productService.findByCategory(category);
    }
    if (minPrice ==0 && maxPrice == 0){
      products = products;
    } else {
      products = products.stream().filter(product -> product.getPrice() >= minPrice && product.getPrice() < maxPrice)
              .collect(Collectors.toList());
    }
    if ("price_low".equalsIgnoreCase(sort)) {
      products.sort(Comparator.comparing(Product::getPrice));
    } else if ("price_high".equalsIgnoreCase(sort)) {
      products.sort(Comparator.comparing(Product::getPrice).reversed());
    }
    return ResponseEntity.ok(products);
  }

  @GetMapping("/filterv")
  public ResponseEntity<List<Product>> findByfilter(@RequestParam String search, @RequestParam String category, @RequestParam Double minPrice, @RequestParam Double maxPrice, @RequestParam String sort) {
//    try {
//      // Giải mã tham số category
//      category = URLDecoder.decode(category, "UTF-8");
//      sort = URLDecoder.decode(sort, "UTF-8");
//
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }

    List<Product> products = productService.findAll();
    if (search != null || !search.isEmpty()) {
      search = search.replace("+", " ");
      products = productService.searchProduct(search);
    }

    if (category == null || category.isEmpty()) {
      products = products;
    } else {
      category = category.replace("+", " ");
      List<Product> categoryFilteredProducts = productService.findByCategory(category);

      if (products != null) {
        // Lọc sản phẩm theo cả tên và thể loại
        products = products.stream()
                .filter(categoryFilteredProducts::contains)
                .collect(Collectors.toList());
      }
    }
    if (minPrice ==0 && maxPrice == 0){
      products = products;
    } else {
      products = products.stream().filter(product -> product.getPrice() >= minPrice && product.getPrice() < maxPrice)
              .collect(Collectors.toList());
    }
    if ("price_low".equalsIgnoreCase(sort)) {
      products.sort(Comparator.comparing(Product::getPrice));
    } else if ("price_high".equalsIgnoreCase(sort)) {
      products.sort(Comparator.comparing(Product::getPrice).reversed());
    }
    return ResponseEntity.ok(products);
  }
}
