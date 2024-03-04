package com.francisco.simple.ecommerce.interfaces;

import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;

import java.util.List;

public interface CartRepositoryInterface {
    Cart createCart();
    Cart getCartById(String id);
    Cart addProductsToCart(Cart cart, List<Product> products);
    String deleteCart(Cart cart);
}
