package com.francisco.simple.ecommerce.services;

import com.francisco.simple.ecommerce.exceptions.ResourceNotFoundException;
import com.francisco.simple.ecommerce.interfaces.CartServiceInterface;
import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;
import com.francisco.simple.ecommerce.repositories.CartRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartServiceInterface {
    @Autowired
    private CartRepositoryImpl cartRepository;

    public Cart createCart() {
        return cartRepository.createCart();
    }

    public Cart getCartById(String id) {
        return cartRepository.getCartById(id);
    }

    public Cart addProductsToCart(String cartId, List<Product> products) {
        try {
            Cart cart = getCartById(cartId);
            return cartRepository.addProductsToCart(cart, products);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Error adding products to cart: " + e.getMessage());
        }
    }

    public String deleteCart(String id) {
        Cart cart = getCartById(id);
        return cartRepository.deleteCart(cart);
    }
}
