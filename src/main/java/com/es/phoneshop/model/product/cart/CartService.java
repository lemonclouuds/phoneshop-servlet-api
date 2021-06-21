package com.es.phoneshop.model.product.cart;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public interface CartService {
    Cart getCart(HttpSession session);
    void addProductToCart(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void updateCart(Cart cart, Long productId, int quantity) throws OutOfStockException;
    void deleteProductFromCart(Cart cart, Long productId);

    Optional<CartItem> findCartItem(Cart cart, Long productId, int quantity) throws OutOfStockException;

    void clearCart(Cart cart);
}
