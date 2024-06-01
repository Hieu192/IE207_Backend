package com.thuctap.fastfood.services;

import com.thuctap.fastfood.entities.Category;
import com.thuctap.fastfood.repositories.CategoryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public Optional<Category> findById(Integer id) {
    return categoryRepository.findById(id);
  }

  public Category save(Category category) {
    return categoryRepository.save(category);
  }

  public void delete(Category category) {
    categoryRepository.delete(category);
  }

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }
}
