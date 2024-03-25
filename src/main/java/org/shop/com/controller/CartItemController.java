package org.shop.com.controller;

import lombok.extern.slf4j.Slf4j;
import org.shop.com.dto.CartItemCreateDto;
import org.shop.com.dto.CartItemDto;
import org.shop.com.exceptions.CartItemInvalidArgumentException;
import org.shop.com.exceptions.CartItemNotFoundException;
import org.shop.com.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<CartItemDto> addCartItem(@RequestBody CartItemCreateDto cartItemCreateDto,
                                                   @RequestParam Long cartId) {
        log.info("Adding cart item with productId: {} and quantity: {} to cartId: {}",
                cartItemCreateDto.getProductId(), cartItemCreateDto.getQuantity(), cartId);
        CartItemDto cartItemDto = cartItemService.addCartItem(cartItemCreateDto, cartId);
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        log.info("Removing cart item with id: {}", cartItemId);
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemDto>> getCartItemsByCartId(@PathVariable Long cartId) {
        log.info("Retrieving items for cart id: {}", cartId);
        List<CartItemDto> cartItems = cartItemService.getCartItemsByCartId(cartId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemDto> updateCartItemQuantity(@PathVariable Long cartItemId, @RequestParam Integer quantity) {
        log.info("Updating quantity of cart item id: {} to {}", cartItemId, quantity);
        CartItemDto cartItemDto = cartItemService.updateCartItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(cartItemDto);
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartItemNotFoundException(CartItemNotFoundException ex, WebRequest request) {
        log.error("Cart item not found exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartItemInvalidArgumentException.class)
    public ResponseEntity<String> handleCartItemInvalidArgumentException(CartItemInvalidArgumentException ex, WebRequest request) {
        log.error("Cart item invalid argument exception: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}