package com.francisco.simple.ecommerce.models;

import com.francisco.simple.ecommerce.exceptions.InvalidProductException;
import com.francisco.simple.ecommerce.exceptions.InvalidProductStringFieldException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Product {
    private final int id;
    private final String description;
    private int amount;

    public Product(int id, String description, int amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        validate();
    }

    public void setAmount(int amount) {
        if(amount <= 0) {
            throw new InvalidProductException("Amount must be greater than 0");
        }
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id == product.id && Double.compare(product.amount, amount) == 0 && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, amount);
    }

    private void validate() {
        if(this.description == null) {
            throw new InvalidProductException("Description must not be null");
        }

        if(this.amount == 0) {
            throw new InvalidProductException("Amount must be greater than 0");
        }

        if(Objects.equals(this.description, "")) {
            throw new InvalidProductStringFieldException("description", "Description must not be empty");
        }
    }
}
