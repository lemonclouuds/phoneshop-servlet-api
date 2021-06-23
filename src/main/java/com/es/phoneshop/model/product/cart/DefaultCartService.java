package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class DefaultCartService implements CartService{
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ProductDao productDao;

    private static volatile CartService instance;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }


    public static CartService getInstance() {
        CartService localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultCartService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public synchronized Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void addProductToCart(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (quantity <= 0) {
            throw new IllegalArgumentException();
        }

        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        Optional<CartItem> result = findCartItem(cart, productId, quantity);

        if (result.isPresent()) {
            result.get().setQuantity(result.get().getQuantity() + quantity);
            recalculateCart(cart);
            return;
        }
        CartItem cartItem = new CartItem(product, quantity);
        cart.getItems().add(cartItem);
        recalculateCart(cart);
    }

    @Override
    public void updateCart(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (quantity <= 0) {
            throw new IllegalArgumentException();
        }

        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        Optional<CartItem> result = findCartItem(cart, productId, quantity);

        if (result.isPresent()) {
            result.get().setQuantity(quantity);
            recalculateCart(cart);
        }
    }

    @Override
    public void deleteProductFromCart(Cart cart, Long productId) {
        cart.getItems().removeIf(cartItem -> productId.equals(cartItem.getProduct().getId()));
        recalculateCart(cart);
    }

    @Override
    public Optional<CartItem> findCartItem(Cart cart, Long productId, int quantity) {
        return cart.getItems().stream()
                .filter(cartItem1 -> cartItem1.getProduct().getId().equals(productId))
                .findAny();
    }

    @Override
    public void clearCart(Cart cart) {
        cart.setItems(new ArrayList<>());
        cart.setTotalCost(BigDecimal.ZERO);
        cart.setTotalQuantity(0);
    }

    private void recalculateCart(Cart cart){
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(q -> q)
                .sum()
        );

        cart.setTotalCost(cart.getItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}
