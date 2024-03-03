package com.francisco.simple.ecommerce.interfaces;

import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;

import java.util.List;
import java.util.Optional;

public interface CartService {
    Optional<Cart> getCartById(String id);
    Cart createCart();
    Optional<Cart> addProductsToCart(String cartId, List<Product> products);
    String deleteCart(String id);
}
