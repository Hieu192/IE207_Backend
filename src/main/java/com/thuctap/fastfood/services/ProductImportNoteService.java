package com.thuctap.fastfood.services;

import com.thuctap.fastfood.entities.ProductImportNote;
import com.thuctap.fastfood.repositories.ProductImportNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductImportNoteService {
  private final ProductImportNoteRepository productImportNoteRepository;

  public ProductImportNote save(ProductImportNote importNote) {
    return productImportNoteRepository.save(importNote);
  }
}
