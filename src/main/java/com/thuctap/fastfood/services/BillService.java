package com.thuctap.fastfood.services;

import com.thuctap.fastfood.dto.BillDTO;
import com.thuctap.fastfood.entities.Bill;
import com.thuctap.fastfood.entities.ProductExportNote;
import com.thuctap.fastfood.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class BillService {
  private final BillRepository billRepository;
  private final ProductExportNoteRepository exportNoteRepository;

  public Optional<Bill> findById(Integer id) {
    return billRepository.findById(id);
  }

  public Bill save(Bill bill) {
    return billRepository.save(bill);
  }

  public List<Bill> findAllBills(String status) {
    return billRepository.findBillsByStatus(status);
  }

  public ProductExportNote save(ProductExportNote exportNote) {
    return exportNoteRepository.save(exportNote);
  }
}
