package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class CartServiceTest {
    private ProductDao productDao;
    private CartService cartService;
    Currency usd = Currency.getInstance("USD");
    int quantity;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Test
    public void testAddProductToCart() throws OutOfStockException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);

        quantity = 5;
        Cart cart = new Cart();
        CartItem cartItem = new CartItem(product, quantity);
        cartService.addProductToCart(cart, product.getId(), quantity);

        assertTrue(cart.getItems().contains(cartItem));
    }

    @Test
    public void testAddSameProductToCart() throws OutOfStockException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);

        quantity = 5;
        int newQuantity = 8;
        Cart cart = new Cart();
        CartItem cartItem = new CartItem(product, quantity);
        cartService.addProductToCart(cart, product.getId(), quantity);

        assertTrue(cart.getItems().contains(cartItem));

        cartService.addProductToCart(cart, product.getId(), newQuantity);

        assertTrue(cart.getItems().get(0).getQuantity() == (quantity + newQuantity));
    }

    @Test (expected = OutOfStockException.class)
    public void testFailedAddProductToCart() throws OutOfStockException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);

        quantity = 15;
        Cart cart = new Cart();
        cartService.addProductToCart(cart, product.getId(), quantity);
    }

    @Test
    public void testGetProduct(){
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);
        quantity = 5;
        CartItem cartItem = new CartItem(product, quantity);

        assertEquals(cartItem.getProduct(), product);
    }
}
