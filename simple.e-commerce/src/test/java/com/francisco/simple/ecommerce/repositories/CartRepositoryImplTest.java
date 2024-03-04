package com.francisco.simple.ecommerce.repositories;

import com.francisco.simple.ecommerce.exceptions.ResourceNotFoundException;
import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.TaskScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CartRepositoryImplTest {
    @Mock
    private TaskScheduler taskScheduler;
    @InjectMocks
    private CartRepositoryImpl cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart() {
        Cart createdCart = cartRepository.createCart();
        assertNotNull(createdCart);
        assertEquals(1, cartRepository.getCarts().size());
    }

    @Test
    void testGetCartById() {
        Cart cart = cartRepository.createCart();
        Cart foundCart = cartRepository.getCartById(cart.getId());

        assertNotNull(foundCart);
        assertEquals(cart, foundCart);
    }

    @Test
    void testGetCartByIdNotFound() {
        String cartIdNotValid = "no-id";

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartRepository.getCartById(cartIdNotValid);
        });

        assertEquals("Cart with id " + cartIdNotValid + " not found", exception.getMessage());
    }

    @Test
    void testAddProductsToCart() {
        Cart cart = cartRepository.createCart();
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product1", 12));

        Cart updateCart = cartRepository.addProductsToCart(cart, products);

        assertNotNull(updateCart);
        assertTrue(updateCart.getProducts().containsAll(products));
        assertEquals(products, updateCart.getProducts());
    }

    @Test
    void testDeleteCart() {
        Cart cart = cartRepository.createCart();
        String result = cartRepository.deleteCart(cart);

        assertEquals("The cart " + cart.getId() + " has been deleted", result);
        assertTrue(cartRepository.getCarts().isEmpty());
    }
}
