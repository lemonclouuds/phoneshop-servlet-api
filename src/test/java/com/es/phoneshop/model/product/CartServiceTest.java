package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.junit.Assert.*;

public class CartServiceTest {
    private ProductDao productDao;
    private CartService cartService;
    Currency usd = Currency.getInstance("USD");
    int quantity = 5;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Test
    public void testAddProductToCart() throws OutOfStockException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);

        Cart cart = new Cart();
        CartItem cartItem = new CartItem(product, quantity);
        cartService.addProductToCart(cart, product.getId(), quantity);

        assertTrue(cart.getItems().contains(cartItem));
    }

    @Test
    public void testAddSameProductToCart() throws OutOfStockException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);

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
        CartItem cartItem = new CartItem(product, quantity);

        assertEquals(cartItem.getProduct(), product);
    }

    @Test
    public void testDeleteProductFromCart() throws OutOfStockException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Cart cart = new Cart();
        CartItem cartItem = new CartItem(product, quantity);

        productDao.save(product);
        cartService.addProductToCart(cart, product.getId(), quantity);

        cartService.deleteProductFromCart(cart, product.getId());
        assertFalse(cart.getItems().contains(cartItem));
    }

    @Test
    public void testFindCartItem() throws OutOfStockException {
        Product product = new Product("product1", "Test", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Cart cart = new Cart();
        CartItem cartItem = new CartItem(product, quantity);

        productDao.save(product);
        cartService.addProductToCart(cart, product.getId(), quantity);

        Optional<CartItem> result = cartService.findCartItem(cart, product.getId(), quantity);
        if (result.isPresent()) {
            assertEquals(result.get(), cartItem);
        }
    }

    @Test
    public void testUpdateCart() throws OutOfStockException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Cart cart = new Cart();

        productDao.save(product);
        cartService.addProductToCart(cart, product.getId(), 3);
        cartService.updateCart(cart, product.getId(), 5);

        assertEquals(5, cart.getItems().get(0).getQuantity());
    }
}
