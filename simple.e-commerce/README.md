# Simple E-Commerce Application

Welcome to the Simple E-Commerce Application! This application has been designed by Francisco Salgado Belmonte.

# API Documentation

Detailed API documentation is at the following Postman link: https://documenter.getpostman.com/view/27347338/2sA2xb7brY

# Main Features
- ### Create a Cart

POST /carts: Create a new shopping cart.

- ### Get a Cart by ID

GET /carts/{cartId}: Gets the information of a specific cart by its ID.

- ### Add Product to a Cart

POST /carts/{cartId}/product: Add a product to the specified cart.

- ### Add Products to a Cart

POST /carts/{cartId}/products: Adds products to the specified cart.

- ### Delete a Cart

DELETE /carts/{cartId}: Delete a cart by its ID.