package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartItem;
import com.es.phoneshop.model.product.order.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderServiceTest {
    private OrderService orderService;
    private OrderDao orderDao;

    @Before
    public void setup() {
        orderService = DefaultOrderService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test
    public void testPlaceOrder() {
        Order order = new Order();
        String secureId = "imsad";

        orderService.placeOrder(order);
        order.setSecureId(secureId);

        assertEquals(order, orderDao.getOrderBySecureId(secureId));
    }

    @Test
    public void testGetOrder() {
        Order order = new Order();
        Long id = 1L;

        orderService.placeOrder(order);
        order.setId(id);
        Order actual = orderDao.getOrder(id);

        assertEquals(order, actual);
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderOrderNotFoundException() {
        Order order = new Order();
        Long id = 2L;

        orderService.placeOrder(order);
        order.setId(id);

        assertEquals(order, orderDao.getOrder(2L));
    }

    @Test
    public void testGetOrderFromCart() {
        Cart cart = new Cart();
        Order order = new Order();
        CartItem cartItem = new CartItem(new Product(), 2);

        List<CartItem> cartItemList = Arrays.asList(cartItem);
        cart.setItems(cartItemList);
        cart.setTotalCost(new BigDecimal(22));
        order.setItems(cartItemList);

        assertEquals(order.getItems(), orderService.getOrder(cart).getItems());
    }
}
