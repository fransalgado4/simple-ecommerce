package com.francisco.simple.ecommerce.repositories;

import com.francisco.simple.ecommerce.exceptions.ResourceNotFoundException;
import com.francisco.simple.ecommerce.interfaces.CartRepository;
import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;
import lombok.Getter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Repository
public class CartRepositoryImpl implements CartRepository {
    private final TaskScheduler taskScheduler;

    @Getter
    private final Map<String, ScheduledFuture<?>> cartDeletionTasks = new HashMap<>();

    @Getter
    private final List<Cart> carts = new ArrayList<>();

    public CartRepositoryImpl(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public Cart createCart() {
        Cart cart = new Cart(UUID.randomUUID().toString(), Collections.emptyList());
        carts.add(cart);

        scheduleCartDeletion(cart);

        return cart;
    }

    public Optional<Cart> getCartById(String id) {
        return carts.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Optional<Cart> addProductsToCart(String cartId, List<Product> productsToAdd) {
        Optional<Cart> optionalCart = getCartById(cartId);

        if(optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<Product> existingProducts = cart.getProducts();
            List<Product> newProducts = new ArrayList<>(existingProducts);

            for (Product newProduct : productsToAdd) {
                Optional<Product> existingProduct = hasCartThisProduct(cartId, newProduct);

                if (existingProduct.isEmpty()) {
                    Product product = new Product(newProducts.size() + 1, newProduct.getDescription(), newProduct.getAmount());
                    newProducts.add(product);
                } else {
                    Product product = existingProduct.get();
                    product.setAmount(product.getAmount() + newProduct.getAmount());
                }
            }
            rescheduleCartDeletion(cart);
            cart.setProducts(newProducts);

        } else {
            throw new ResourceNotFoundException("Cart " + cartId + " not found");
        }

        return optionalCart;
    }

    public String deleteCart(String cartId) {
        Optional<Cart> optionalCart = getCartById(cartId);

        if(optionalCart.isPresent()) {
            String cartRemoveId = optionalCart.get().getId();

            carts.removeIf(cart -> cart.getId().equals(cartRemoveId));
            cancelScheduledTask(cartId);

            return "The cart " + cartRemoveId + " has been deleted";
        } else {
            throw new ResourceNotFoundException("Cart " + cartId + " not found");
        }
    }

    private Optional<Product> hasCartThisProduct(String cartId, Product product) {
        Optional<Cart> optionalCart = getCartById(cartId);

        if(optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<Product> existingProducts = cart.getProducts();

            for (Product existingProduct : existingProducts) {
                if (existingProduct.getDescription().equalsIgnoreCase(product.getDescription())) {
                    return Optional.of(existingProduct);
                }
            }

            return Optional.empty();
        } else {
            throw new ResourceNotFoundException("Cart " + cartId + " not found");
        }
    }

    private void scheduleCartDeletion(Cart cart) {
        String cartId = cart.getId();
        Date dateToExpire = new Date(System.currentTimeMillis() + Duration.ofMinutes(10).toMillis());
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(() -> deleteCart(cartId), dateToExpire);
        cartDeletionTasks.put(cartId, scheduledFuture);
    }

    private void rescheduleCartDeletion(Cart cart) {
        cancelScheduledTask(cart.getId());
        scheduleCartDeletion(cart);
    }

    private void cancelScheduledTask(String cartId) {
        ScheduledFuture<?> scheduledFuture = cartDeletionTasks.get(cartId);
        if(scheduledFuture != null) {
            scheduledFuture.cancel(false);
            cartDeletionTasks.remove(cartId);
        }
    }
}
