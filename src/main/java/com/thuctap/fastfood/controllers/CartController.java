package com.thuctap.fastfood.controllers;

import com.thuctap.fastfood.dto.CartProductDTO;
import com.thuctap.fastfood.dto.RemoveProductFromCartDTO;
import com.thuctap.fastfood.dto.SaveProductToCartDTO;
import com.thuctap.fastfood.entities.Account;
import com.thuctap.fastfood.entities.Cart;
import com.thuctap.fastfood.entities.CartProduct;
import com.thuctap.fastfood.entities.Product;
import com.thuctap.fastfood.entities.embeddedId.CartProductKey;
import com.thuctap.fastfood.services.AccountService;
import com.thuctap.fastfood.services.CartService;
import com.thuctap.fastfood.services.ProductService;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

  private final AccountService accountService;
  private final ProductService productService;
  private final CartService cartService;

  @GetMapping
  public ResponseEntity<List<CartProductDTO>> getCartProduct(@RequestParam Integer accountId) {
    List<CartProductDTO> result = new ArrayList<>();
    Optional<Account> accountOptional = accountService.findById(accountId);
    if (accountOptional.isPresent()) {
      List<CartProduct> cartProducts =
          cartService.findAllProductsInCart(accountOptional.get().getCart());
      cartProducts.forEach(
          cartProduct -> {
            CartProductDTO cartProductDTO =
                CartProductDTO.builder()
                    .productId(cartProduct.getProduct().getId())
                    .quantity(cartProduct.getQuantity())
                    .build();
            result.add(cartProductDTO);
          });
    }
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<Map<String, Object>> saveProductToCart(
      @RequestBody SaveProductToCartDTO body) {
    Map<String, Object> returnedMap = new HashMap<>();
    if (body.getAccountId() != null) {
      Optional<Account> accountOptional = accountService.findById(body.getAccountId());
      if (accountOptional.isPresent()) {
        Account account = accountOptional.get();
        Cart cart = account.getCart();
        body.getCartProductDTOS()
            .forEach(
                cartProductDTO -> {
                  Optional<Product> productOptional =
                      productService.findById(cartProductDTO.getProductId());
                  productOptional.ifPresent(
                      product -> {
                        Optional<CartProduct> cartProductOptional =
                            cartService.findCartProduct(cart, product);
                        CartProduct cartProduct;
                        if (cartProductOptional.isPresent()) {
                          cartProduct = cartProductOptional.get();
                          cartProduct.setQuantity(cartProductDTO.getQuantity());
                        } else {
                          cartProduct = new CartProduct();
                          cartProduct.setCart(cart);
                          cartProduct.setProduct(product);
                        }
                        cartProduct.setQuantity(cartProductDTO.getQuantity());
                        cartService.saveProductToCart(cartProduct);
                      });
                });
        returnedMap.put("isSuccessful", true);
        return ResponseEntity.ok(returnedMap);
      }
    }
    returnedMap.put("error", "Cần truyền vào id của tài khoản");
    return ResponseEntity.ok(returnedMap);
  }


  @PostMapping("/delete")
  public ResponseEntity<Boolean> removeProductFromCart(
      @RequestBody RemoveProductFromCartDTO removeProductFromCartDTO) {
    Optional<Account> accountOptional =
        accountService.findById(removeProductFromCartDTO.getAccountId());
    if (accountOptional.isPresent()) {
      Cart cart = accountOptional.get().getCart();
      CartProductKey cartProductKey = new CartProductKey();
      cartProductKey.setCartId(cart.getId());
      cartProductKey.setProductId(removeProductFromCartDTO.getProductId());
      Optional<CartProduct> cartProductOptional = cartService.findCartProductById(cartProductKey);
      if (cartProductOptional.isPresent()) {
        cartService.removeProductFromCart(cartProductOptional.get());
        return ResponseEntity.ok(true);
      }
    }
    return ResponseEntity.ok(false);
  }
}
