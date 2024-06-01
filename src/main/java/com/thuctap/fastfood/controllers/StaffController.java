package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.dto.AccountDTO;
import com.thuctap.fastfood.dto.ProductImportDTO;
import com.thuctap.fastfood.dto.StaffDTO;
import com.thuctap.fastfood.dto.UserDTO;
import com.thuctap.fastfood.entities.*;
import com.thuctap.fastfood.services.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/staffs")
public class StaffController {
  private final AccountService accountService;
  private final StaffService staffService;
  private final ProductService productService;
  private final ProductImportNoteService productImportNoteService;
  private final BillService billService;
  private final UserService userService;
  private final SupplierService supplierService;
  @PostMapping("/importNote")
  public ResponseEntity<Integer> saveImportNote(@RequestBody ProductImportDTO productImportDTO) {
    Optional<Account> accountOptional = accountService.findById(productImportDTO.getAccountId());
    if (accountOptional.isPresent()) {
      Optional<Staff> staffOptional = staffService.findById(accountOptional.get().getIdPerson());
      if (staffOptional.isPresent()) {
        Staff staff = staffOptional.get();
        Optional<Product> productOptional =
            productService.findById(productImportDTO.getProductId());
        if (productOptional.isPresent()) {
          Product product = productOptional.get();
          Supplier supplier = supplierService.findById(Integer.parseInt(productImportDTO.getSupplier())).get();

          // Lập phiếu nhập hàng
          ProductImportNote importNote = new ProductImportNote();
          importNote.setStaff(staff);
          importNote.setDate(LocalDateTime.now());
          importNote.setSupplier(supplier);

          // lập chi tiết phiếu nhập hàng
          ProductImportNoteDetails importNoteDetails = new ProductImportNoteDetails();
          importNoteDetails.setImportNote(importNote);
          importNoteDetails.setProduct(product);
          importNoteDetails.setQuantity(productImportDTO.getQuantity());
          importNoteDetails.setPrice(productImportDTO.getPrice());

          // Thêm chi tiết vào đơn
          importNote.addProductImportNoteDetails(importNoteDetails);

          // Lưu Đơn
          ProductImportNote returnImportNote = productImportNoteService.save(importNote);
          product.setStock(product.getStock() + productImportDTO.getQuantity());
          productService.saveProduct(product);
          return ResponseEntity.ok(returnImportNote.getId());
        }
      }
    }
    return ResponseEntity.ok(null);
  }

  @GetMapping("/check-email-phone-number")
  public ResponseEntity<Map<String, String>> checkEmailAndPhoneNumber(
          @RequestParam("email") String email,
          @RequestParam("phoneNumber") String phoneNumber,
          @RequestParam("accountId") Integer accountId) {
    Map<String, String> map = new HashMap<>();
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      Optional<Staff> staffOfAccountOptional = staffService.findById(account.getIdPerson());
      if (staffOfAccountOptional.isPresent()) {
        Staff staffOfAccount = staffOfAccountOptional.get();

        Optional<Staff> staffOptional = staffService.findByEmail(email);
        if (staffOptional.isPresent()) {
          Staff staff = staffOptional.get();
          if (!staffOfAccount.getId().equals(staff.getId()))
            map.put("email", "Đã tồn tại tài khoản có email này");
        }
        staffOptional = staffService.findByPhoneNumber(phoneNumber);
        if (staffOptional.isPresent()) {
          Staff staff = staffOptional.get();
          if (!staff.getId().equals(staffOfAccount.getId()))
            map.put("phoneNumber", "Đã tồn tại tài khoản có số điện thoại này");
        }

        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
          map.put("email", "Đã tồn tại tài khoản có email này");
        }

        userOptional = userService.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
          map.put("phoneNumber", "Đã tồn tại tài khoản có số điện thoại này");
        }
      }
    }
    return ResponseEntity.ok(map);
  }

  @GetMapping("/find-staff-by-account-id")
  public ResponseEntity<StaffDTO> findUserByAccountId(@RequestParam("accountId") Integer accountId) {
    StaffDTO staffDTO = null;
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Optional<Staff> staffOptional = staffService.findById(accountOptional.get().getIdPerson());
      if (staffOptional.isPresent()) {
        Staff staff = staffOptional.get();
        staffDTO = staffService.toDTO(staff);
      }
    }
    return ResponseEntity.ok(staffDTO);
  }

  @PostMapping("/update-info-by-account")
  public ResponseEntity<Boolean> updateInfoByAccount(@RequestBody StaffDTO staffDTO, @RequestParam("accountId") Integer accountId) {
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Optional<Staff> staffOptional  = staffService.findById(accountOptional.get().getIdPerson());
      if (staffOptional.isPresent()) {
        Staff staff = staffOptional.get();
        staffService.changeFieldsDTOToEntity(staffDTO, staff);
        staffService.save(staff);
        return ResponseEntity.ok(true);
      }
    }
    return ResponseEntity.ok(false);
  }

  @PostMapping("/payment-confirmation")
  public ResponseEntity<Boolean> paymentConfirmation(@RequestParam("billId") Integer billId, @RequestParam("accountId") Integer accountId) {
    Optional<Bill> billOptional = billService.findById(billId);
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (billOptional.isPresent() && accountOptional.isPresent()) {
      Bill bill = billOptional.get();
      Account account = accountOptional.get();
      Staff staff = staffService.findById(account.getIdPerson()).get();
      bill.setStatus("Đã Thanh Toán");
      bill.setDateSuccessfullyPaid(LocalDateTime.now());
      bill.setStaff(staff);

      // Lập phiếu xuất
      ProductExportNote exportNote = new ProductExportNote();
      exportNote.setDate(LocalDateTime.now());
      exportNote.setBill(bill);

       bill.setExportNote(exportNote);
      billService.save(bill);
//      exportNote =  billService.save(exportNote);
//      bill.setExportNote(exportNote);
      return ResponseEntity.ok(true);
    }

    return ResponseEntity.ok(false);
  }

  @GetMapping("/find-by-id")
  public ResponseEntity<StaffDTO> findStaffById(@RequestParam("staffId") String staffId) {
    Optional<Staff> staffOptional = staffService.findById(staffId);
    if (staffOptional.isPresent()) {
      Staff staff = staffOptional.get();
      StaffDTO dto = staffService.toDTO(staff);
      return ResponseEntity.ok(dto);
    }
    return ResponseEntity.ok(null);
  }

  @GetMapping("/find-account")
  public ResponseEntity<AccountDTO> findAccount(@RequestParam("staffId") String staffId) {
    Optional<Staff> staffOptional = staffService.findById(staffId);
    if (staffOptional.isPresent()) {
      Staff staff = staffOptional.get();
      Optional<Account> accountOptional = accountService.findByIdPerson(staff.getId());
      if (accountOptional.isPresent()) {
        Account account = accountOptional.get();
        AccountDTO dto = accountService.toDTO(account);
        return ResponseEntity.ok(dto);
      }
    }
    return ResponseEntity.ok(null);
  }
}
