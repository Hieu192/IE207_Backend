package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.dto.CategoryDTO;
import com.thuctap.fastfood.entities.Category;
import com.thuctap.fastfood.entities.Product;
import com.thuctap.fastfood.services.CategoryService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<Boolean> saveCategory(@RequestBody CategoryDTO categoryDTO) {
    if (categoryDTO.getId() != null) {
      // Update
      Optional<Category> categoryOptional = categoryService.findById(categoryDTO.getId());
      if (categoryOptional.isPresent()) {
        Category category = categoryOptional.get();
        if (categoryDTO.getDescription() != null)
          category.setDescription(categoryDTO.getDescription());
        if (categoryDTO.getName() != null) category.setName(categoryDTO.getName());
        categoryService.save(category);
        return ResponseEntity.ok(true);
      }
    }
    Category category = new Category();
    category.setDescription(categoryDTO.getDescription());
    category.setName(categoryDTO.getName());
    Category returnedCategory = categoryService.save(category);
    if (returnedCategory.getId() != null) return ResponseEntity.ok(true);
    return ResponseEntity.ok(false);
  }

  @GetMapping
  public ResponseEntity<List<Category>> getAllCategories() {
    return ResponseEntity.ok(categoryService.findAll());
  }

  @PostMapping("/delete/{categoryId}")
  public ResponseEntity<Map<String, Object>> deleteCategory(
      @PathVariable("categoryId") Integer categoryId) {
    Map<String, Object> map = new HashMap<>();
    Optional<Category> categoryOptional = categoryService.findById(categoryId);
    if (categoryOptional.isPresent()) {
      List<Product> products = categoryOptional.get().getProducts();
      if (!products.isEmpty()) {
        map.put("isSuccessful", false);
        map.put("error", "Đã có sản phẩm được thuộc danh mục này nên không thể xóa");
        return ResponseEntity.ok(map);
      }
      categoryService.delete(categoryOptional.get());
      map.put("isSuccessful", true);
      return ResponseEntity.ok(map);
    }
    map.put("isSuccessful", false);
    map.put("error", "Không có danh mục thuộc" + categoryId + " đã đưa");
    return ResponseEntity.ok(map);
  }
}
