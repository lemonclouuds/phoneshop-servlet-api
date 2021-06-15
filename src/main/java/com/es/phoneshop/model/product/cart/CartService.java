package com.es.phoneshop.model.product.cart;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void addProductToCart(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void updateCart(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void deleteProductFromCart(Cart cart, Long productId);

    Optional<CartItem> findCartItem(Cart cart, Long productId, int quantity) throws OutOfStockException;
}
