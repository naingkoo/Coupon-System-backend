package com.coupon.controller;

import com.coupon.entity.CartEntity;
import com.coupon.model.CartDTO;
import com.coupon.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody CartDTO cartDTO) {
        cartService.addItemToCart(cartDTO);
        return ResponseEntity.ok(cartDTO);
    }

    @GetMapping("/public/list/{userId}")
    public ResponseEntity<List<CartDTO>> getCartByUserId(@PathVariable Integer userId) {
        List<CartDTO> cartDetails = cartService.getCartByUserId(userId);
        if (cartDetails.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no cart items found
        }
        return ResponseEntity.ok(cartDetails); // Return 200 OK with cart details
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Integer id) {
        boolean isRemoved = cartService.deleteCartItem(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PutMapping("/increase/{cartId}")
    public ResponseEntity<CartEntity> increaseQuantity(@PathVariable Integer cartId) {
        CartEntity updatedCart = cartService.increaseQuantity(cartId);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.notFound().build(); // Return 404 if not found
    }

    @PutMapping("/decrease/{cartId}")
    public ResponseEntity<CartEntity> decreaseQuantity(@PathVariable Integer cartId) {
        CartEntity updatedCart = cartService.decreaseQuantity(cartId);
        if (updatedCart != null) {
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.notFound().build(); // Return 404 if not found
    }
}