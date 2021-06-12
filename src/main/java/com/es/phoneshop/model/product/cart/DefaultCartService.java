package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.http.HttpServletRequest;

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
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void addProductToCart(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        CartItem cartItem = new CartItem(product, quantity);

        if (cart.getItems().contains(cartItem)) {
            CartItem result = cart.getItems().stream()
                    .filter(cartItem1 -> cartItem1.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new ProductNotFoundException(productId));
            result.setQuantity(result.getQuantity() + quantity);
            product.setStock(product.getStock() - quantity);
            return;
        }
        cart.getItems().add(cartItem);
        product.setStock(product.getStock() - quantity);
    }
}
