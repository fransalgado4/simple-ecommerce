package com.francisco.simple.ecommerce.controllers;

import com.francisco.simple.ecommerce.exceptions.InvalidProductException;
import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;
import com.francisco.simple.ecommerce.services.CartServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/store/carts")
public class CartController {
    private final CartServiceImpl cartService;

    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable String id) {
        Cart cart = cartService.getCartById(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCart() {
        return new ResponseEntity<>(cartService.createCart(), HttpStatus.OK);
    }

    @PutMapping("/{id}/product")
    public ResponseEntity<?> addProductToCart(@PathVariable String id, @RequestBody Product product) {
        try {
            List<Product> products = new ArrayList<>();
            products.add(product);
            return new ResponseEntity<>(cartService.addProductsToCart(id, products), HttpStatus.OK);
        } catch (InvalidProductException e) {
            throw new InvalidProductException(e.getMessage());
        }
    }

    @PutMapping("/{id}/products")
    public ResponseEntity<?> addProductsToCart(@PathVariable String id, @RequestBody List<Product> products) {
        try {
            return new ResponseEntity<>(cartService.addProductsToCart(id, products), HttpStatus.OK);
        } catch (InvalidProductException e) {
            throw new InvalidProductException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable String id) {
        return new ResponseEntity<>(cartService.deleteCart(id), HttpStatus.OK);
    }
}
