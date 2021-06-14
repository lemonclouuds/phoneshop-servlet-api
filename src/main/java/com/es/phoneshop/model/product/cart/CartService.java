package com.es.phoneshop.model.product.cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void addProductToCart(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void updateCart(Cart cart, Long productId, int quantity) throws OutOfStockException;
}
