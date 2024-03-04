package com.francisco.simple.ecommerce.repositories;

import com.francisco.simple.ecommerce.exceptions.ResourceNotFoundException;
import com.francisco.simple.ecommerce.interfaces.CartRepositoryInterface;
import com.francisco.simple.ecommerce.models.Cart;
import com.francisco.simple.ecommerce.models.Product;
import lombok.Getter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Repository
public class CartRepositoryImpl implements CartRepositoryInterface {
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

    public Cart getCartById(String id) {
        return carts.stream().filter(c -> c.getId().equals(id)).findFirst().orElseThrow(
                () -> new ResourceNotFoundException("Cart with id " + id + " not found")
        );
    }

    public Cart addProductsToCart(Cart cart, List<Product> productsToAdd) {
            List<Product> existingProducts = cart.getProducts();
            List<Product> newProducts = new ArrayList<>(existingProducts);

            for (Product newProduct : productsToAdd) {
                Optional<Product> existingProduct = findProductInCart(cart, newProduct);

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

        return cart;
    }

    public String deleteCart(Cart cart) {
        String cartRemoveId = cart.getId();

        carts.removeIf(cartRemove -> cartRemove.getId().equals(cartRemoveId));
        cancelScheduledTask(cartRemoveId);

        return "The cart " + cartRemoveId + " has been deleted";
    }

    private Optional<Product> findProductInCart(Cart cart, Product product) {
        List<Product> existingProducts = cart.getProducts();

        for (Product existingProduct : existingProducts) {
            if (existingProduct.getDescription().equalsIgnoreCase(product.getDescription())) {
                return Optional.of(existingProduct);
            }
        }

        return Optional.empty();
    }

    private void scheduleCartDeletion(Cart cart) {
        Date dateToExpire = new Date(System.currentTimeMillis() + Duration.ofMinutes(10).toMillis());
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(() -> deleteCart(cart), dateToExpire);
        cartDeletionTasks.put(cart.getId(), scheduledFuture);
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
