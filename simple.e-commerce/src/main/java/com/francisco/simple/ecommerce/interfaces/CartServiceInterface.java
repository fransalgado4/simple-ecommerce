package com.francisco.simple.ecommerce.interfaces;

import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;

import java.util.List;

public interface CartServiceInterface {
    Cart getCartById(String id);
    Cart createCart();
    Cart addProductsToCart(String cartId, List<Product> products);
    String deleteCart(String id);
}
