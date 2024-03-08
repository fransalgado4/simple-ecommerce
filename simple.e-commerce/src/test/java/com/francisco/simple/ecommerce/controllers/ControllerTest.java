package com.francisco.simple.ecommerce.controllers;

import com.francisco.simple.ecommerce.exceptions.InvalidProductException;
import com.francisco.simple.ecommerce.exceptions.ResourceNotFoundException;
import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;
import com.francisco.simple.ecommerce.services.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControllerTest {
    @Mock
    private CartServiceImpl cartService;
    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCart() {
        String cartId = "1234354";
        Cart mockCart = new Cart();

        when(cartService.getCartById(cartId)).thenReturn(mockCart);

        ResponseEntity<?> responseEntity = cartController.getCart(cartId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Optional.of(mockCart).get(), responseEntity.getBody());
    }

    @Test
    void testAddProductToCart() {
        String cartId = "1234354";
        Product product = new Product(1, "prueba", 12);
        List<Product> products = Arrays.asList((product));
        Cart mockCart = new Cart();

        when(cartService.addProductsToCart(cartId, products)).thenReturn(mockCart);

        ResponseEntity<?> responseEntity = cartController.addProductToCart(cartId, product);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Optional.of(mockCart), Optional.ofNullable(responseEntity.getBody()));
    }

    @Test
    void testAddProductToCartWithInvalidProductException() {
        String cartId = "1234354";
        Product mockProduct = new Product(1, "prueba", 12);
        List<Product> products = new ArrayList<>();
        products.add(mockProduct);

        when(cartService.addProductsToCart(cartId, products)).thenThrow(new InvalidProductException("Invalid product"));

        InvalidProductException exception = assertThrows(InvalidProductException.class, () -> {
            cartController.addProductsToCart(cartId, products);
        });

        assertEquals("Invalid product", exception.getMessage());
    }

    @Test
    void testDeleteCart() {
        String cartId = "1234354";

        when(cartService.deleteCart(cartId)).thenReturn("The cart " + cartId + " has been deleted");

        ResponseEntity<?> responseEntity = cartController.deleteCart(cartId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The cart " + cartId + " has been deleted", responseEntity.getBody());
    }

    @Test
    void testDeleteCartNotFound() {
        String cartId = "1234354";

        when(cartService.deleteCart(cartId)).thenThrow(new ResourceNotFoundException("Cart " + cartId + " not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartController.deleteCart(cartId);
        });

        assertEquals("Cart " + cartId + " not found", exception.getMessage());
    }
}
