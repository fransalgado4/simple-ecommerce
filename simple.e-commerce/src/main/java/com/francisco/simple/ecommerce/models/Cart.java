package com.francisco.simple.ecommerce.models;

import com.francisco.simple.ecommerce.exceptions.ResourceNotFoundException;
import com.francisco.simple.ecommerce.interfaces.CartService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private String id;
    private List<Product> products;
}
