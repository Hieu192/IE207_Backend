package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.dto.BillDTO;
import com.thuctap.fastfood.dto.CartProductDTO;
import com.thuctap.fastfood.entities.*;
import com.thuctap.fastfood.services.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bills")
public class BillController {
  private final BillService billService;
  private final AccountService accountService;
  private final ProductService productService;
  private final UserService userService;
  private final CartService cartService;
  private final StaffService staffService;

  @GetMapping
  public ResponseEntity<List<BillDTO>> getAllBillsOfAccount(
      @RequestParam("accountId") Integer accountId) {
    List<BillDTO> billDTOS = new ArrayList<>();
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      Optional<User> userOptional = userService.findById(account.getIdPerson());
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        user.getBills()
            .forEach(
                bill -> {
                  BillDTO dto = new BillDTO();
                  entityToDTO(bill, dto);
                  billDTOS.add(dto);
                });
      }
    }
    return ResponseEntity.ok(billDTOS);
  }

  @GetMapping("successful-bills")
  public ResponseEntity<List<BillDTO>> getAllSuccessfulBills(
      @RequestParam("accountId") Integer accountId) {
    List<BillDTO> billDTOS = new ArrayList<>();
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      Staff staff = staffService.findById(accountOptional.get().getIdPerson()).orElse(null);
      if (staff != null) {
        billService
            .findAllBills("Đã Thanh Toán")
            .forEach(
                bill -> {
                  if (bill.getStaff().getId().equals(staff.getId())) {
                    BillDTO billDTO =
                        BillDTO.builder()
                            .billId(bill.getId())
                            .paymentMethod(bill.getPaymentMethod())
                            .status(bill.getStatus())
                            .totalPrice(bill.getTotalPrice())
                            .dateCreated(bill.getDateCreated())
                            .dateSuccessfullyPaid(bill.getDateSuccessfullyPaid())
                            .address(bill.getAddress())
                            .phoneNumber(bill.getPhoneNumber())
                            .notes(bill.getNotes())
                            .qrPaymentPath(bill.getQrPaymentPath())
                            .build();
                    billDTOS.add(billDTO);
                  }
                });
      }
    }

    return ResponseEntity.ok(billDTOS);
  }

  @GetMapping("waiting-bills")
  public ResponseEntity<List<BillDTO>> getAllWaitingBills() {
    List<BillDTO> billDTOS = new ArrayList<>();
    billService
        .findAllBills("Đang Chờ Duyệt")
        .forEach(
            bill -> {
              BillDTO billDTO =
                  BillDTO.builder()
                      .billId(bill.getId())
                      .paymentMethod(bill.getPaymentMethod())
                      .status(bill.getStatus())
                      .totalPrice(bill.getTotalPrice())
                      .dateCreated(bill.getDateCreated())
                      .dateSuccessfullyPaid(bill.getDateSuccessfullyPaid())
                      .address(bill.getAddress())
                      .phoneNumber(bill.getPhoneNumber())
                      .notes(bill.getNotes())
                      .qrPaymentPath(bill.getQrPaymentPath())
                      .build();
              billDTOS.add(billDTO);
            });

    return ResponseEntity.ok(billDTOS);
  }

  private void entityToDTO(Bill entity, BillDTO dto) {
    dto.setBillId(entity.getId());
    dto.setTotalPrice(entity.getTotalPrice());
    dto.setPaymentMethod(entity.getPaymentMethod());
    dto.setStatus(entity.getStatus());
    dto.setDateCreated(entity.getDateCreated());
  }

  @GetMapping("/{billId}")
  public ResponseEntity<BillDTO> findBillById(@PathVariable Integer billId) {
    Optional<Bill> billOptional = billService.findById(billId);
    BillDTO billDTO = null;
    if (billOptional.isPresent()) {
      Bill bill = billOptional.get();
      billDTO =
          BillDTO.builder()
              .billId(bill.getId())
              .paymentMethod(bill.getPaymentMethod())
              .status(bill.getStatus())
              .totalPrice(bill.getTotalPrice())
              .dateCreated(bill.getDateCreated())
              .dateSuccessfullyPaid(bill.getDateSuccessfullyPaid())
              .address(bill.getAddress())
              .phoneNumber(bill.getPhoneNumber())
              .qrPaymentPath(bill.getQrPaymentPath())
              .notes(bill.getNotes())
              .build();
    }

    return ResponseEntity.ok(billDTO);
  }

  @GetMapping("/details")
  public ResponseEntity<List<CartProductDTO>> getBillDetails(
      @RequestParam("billId") Integer billId) {
    Optional<Bill> billOptional = billService.findById(billId);
    List<CartProductDTO> billDetails;
    if (billOptional.isPresent()) {
      Bill bill = billOptional.get();
      billDetails = new ArrayList<>();
      bill.getBillDetails()
          .forEach(
              billDetail -> {
                CartProductDTO cartProductDTO = new CartProductDTO();
                cartProductDTO.setProductId(billDetail.getPrimaryKey().getProductId());
                cartProductDTO.setQuantity(billDetail.getQuantity());
                billDetails.add(cartProductDTO);
              });
    } else {
      billDetails = null;
    }
    return ResponseEntity.ok(billDetails);
  }

  @GetMapping("/paid/{billId}")
  public ResponseEntity<Boolean> successfullyPaid(
      @PathVariable Integer billId, @RequestParam("qr_path") String qrPath) {
    Optional<Bill> billOptional = billService.findById(billId);
    if (billOptional.isPresent()) {
      Bill bill = billOptional.get();
      bill.setStatus("Đang Chờ Duyệt");
      bill.setQrPaymentPath(qrPath);
      billService.save(bill);
      return ResponseEntity.ok(true);
    }
    return ResponseEntity.ok(false);
  }

  @GetMapping("/cancel")
  public ResponseEntity<Boolean> cancelBill(@RequestParam("billId") Integer billId) {
    Optional<Bill> billOptional = billService.findById(billId);
    if (billOptional.isPresent()) {
      Bill bill = billOptional.get();
      bill.getBillDetails()
          .forEach(
              billDetail -> {
                Product product = billDetail.getProduct();
                product.setStock(product.getStock() + billDetail.getQuantity());
                productService.saveProduct(product);
              });
      bill.setStatus("Đã Hủy");
      billService.save(bill);
      return ResponseEntity.ok(true);
    }
    return ResponseEntity.ok(false);
  }

  @PostMapping
  public ResponseEntity<Integer> createBill(@RequestBody BillDTO billDTO) {
    Bill bill = createBillFromDTO(billDTO);

    Optional<Account> accountOptional = accountService.findById(billDTO.getAccountId());
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      Cart cart = account.getCart();
      cartService.clearCartProduct(cart);
      cart.clearCart();
      cartService.save(cart);
    }
    bill = billService.save(bill);

    return ResponseEntity.ok(bill.getId());
  }

  private Bill createBillFromDTO(BillDTO billDTO) {
    Bill bill = new Bill();
    Optional<Account> accountOptional = accountService.findById(billDTO.getAccountId());
    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      Optional<User> userOptional = userService.findById(account.getIdPerson());
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        bill.setUser(user);
        bill.setStatus("Chờ Thanh Toán");
        bill.setDateCreated(LocalDateTime.now());
        bill.setAddress(billDTO.getAddress());
        bill.setPhoneNumber(billDTO.getPhoneNumber());
        bill.setNotes(billDTO.getNotes());
        bill.setPaymentMethod(billDTO.getPaymentMethod());
        bill.setTotalPrice(billDTO.getTotalPrice());

        // nhét bill detail vào bill
        billDTO
            .getCart()
            .forEach(
                cartProductDTO -> {
                  Optional<Product> productOptional =
                      productService.findById(cartProductDTO.getProductId());
                  if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    product.setStock(product.getStock() - cartProductDTO.getQuantity());

                    // Lập chi tiết bill
                    BillDetail billDetail = new BillDetail();
                    billDetail.setBill(bill);
                    billDetail.setProduct(product);
                    billDetail.setQuantity(cartProductDTO.getQuantity());
                    billDetail.setPrice(product.getPrice());

                    // Thêm chi tiết vào bill
                    bill.addBillDetail(billDetail);

                    // Lưu số lượng tồn mới vào product
                    productService.saveProduct(product);
                  }
                });
      }
    }
    return bill;
  }
}
