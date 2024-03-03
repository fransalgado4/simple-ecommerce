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
        Optional<Cart> foundCart = cartRepository.getCartById(cart.getId());

        assertTrue(foundCart.isPresent());
        assertEquals(cart, foundCart.get());
    }

    @Test
    void testGetCartByIdNotFound() {
        Optional<Cart> foundCart = cartRepository.getCartById("no-id");

        assertTrue(foundCart.isEmpty());
    }

    @Test
    void testAddProductsToCart() {
        Cart cart = cartRepository.createCart();
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product1", 12));

        Optional<Cart> updateCart = cartRepository.addProductsToCart(cart.getId(), products);

        assertTrue(updateCart.isPresent());
        assertTrue(updateCart.get().getProducts().containsAll(products));
        assertEquals(products, updateCart.get().getProducts());
    }

    @Test
    void testAddProductsToCartNotFound() {
        String cartId = "no-id";

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartRepository.addProductsToCart(cartId, new ArrayList<>());
        });

        assertEquals("Cart " + cartId + " not found", exception.getMessage());
    }

    @Test
    void testDeleteCart() {
        Cart cart = cartRepository.createCart();
        String result = cartRepository.deleteCart(cart.getId());

        assertEquals("The cart " + cart.getId() + " has been deleted", result);
        assertTrue(cartRepository.getCarts().isEmpty());
    }
}
