package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.dto.SupplierDTO;
import com.thuctap.fastfood.entities.Supplier;
import com.thuctap.fastfood.services.SupplierService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
  private final SupplierService supplierService;

  @GetMapping
  public ResponseEntity<List<SupplierDTO>> getSuppliers(@RequestParam(name ="status", required = false) Boolean status) {

    List<SupplierDTO> result = new ArrayList<>();
    List<Supplier> suppliers;
    if (status != null) {
      suppliers = supplierService.findAllByStatus(status);
    } else suppliers = supplierService.findAll();
    suppliers.forEach(
        supplier -> {
          result.add(supplierService.toDTO(supplier));
        });
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SupplierDTO> getSupplier(@PathVariable Integer id) {
    Optional<Supplier> supplierOptional = supplierService.findById(id);
    if (supplierOptional.isPresent()) {
      Supplier supplier = supplierOptional.get();
      return ResponseEntity.ok(supplierService.toDTO(supplier));
    }
    return ResponseEntity.ok(null);
  }

  @PostMapping
  public ResponseEntity<Boolean> saveSupplier(@RequestBody SupplierDTO dto) {
    Supplier entity = new Supplier();
    entity.setName(dto.getName());
    entity = supplierService.save(entity);
    return ResponseEntity.ok(entity.getId() != null);
  }
}
