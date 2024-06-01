package com.thuctap.fastfood.services;

import com.thuctap.fastfood.dto.SupplierDTO;
import com.thuctap.fastfood.entities.Supplier;
import com.thuctap.fastfood.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SupplierService {
  private final SupplierRepository supplierRepository;

  public SupplierDTO toDTO(Supplier entity) {
    return SupplierDTO.builder()
        .id(entity.getId())
        .name(entity.getName())
        .status(entity.isStatus())
        .build();
  }

  public List<Supplier> findAll() {
    return supplierRepository.findAll();
  }

  public List<Supplier> findAllByStatus(boolean status) {
    return supplierRepository.findAllByStatus(status);
  }

  public Supplier save(Supplier supplier) {
    return supplierRepository.save(supplier);
  }

  public Optional<Supplier> findById(Integer id) {
    return supplierRepository.findById(id);
  }
}
