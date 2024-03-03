package com.francisco.simple.ecommerce.interfaces;

import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;

import java.util.List;
import java.util.Optional;

public interface CartRepository {
    Cart createCart();
    Optional<Cart> getCartById(String id);
    Optional<Cart> addProductsToCart(String cartId, List<Product> products);
    String deleteCart(String id);
}
