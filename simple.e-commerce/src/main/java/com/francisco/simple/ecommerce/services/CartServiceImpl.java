package com.francisco.simple.ecommerce.services;

import com.francisco.simple.ecommerce.exceptions.ResourceNotFoundException;
import com.francisco.simple.ecommerce.interfaces.CartService;
import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;
import com.francisco.simple.ecommerce.repositories.CartRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepositoryImpl cartRepository;

    public Cart createCart() {
        return cartRepository.createCart();
    }

    public Optional<Cart> getCartById(String id) {
        return cartRepository.getCartById(id);
    }

    public Optional<Cart> addProductsToCart(String cartId, List<Product> products) {
        verifyCart(cartId);
        return cartRepository.addProductsToCart(cartId, products);
    }

    public String deleteCart(String id) {
        verifyCart(id);
        return cartRepository.deleteCart(id);
    }

    private void verifyCart(String cartId) {
        Optional<Cart> cart = getCartById(cartId);

        if(cart.isEmpty()) {
            throw new ResourceNotFoundException("Cart with id " + cartId + " not found");
        }
    }
}
