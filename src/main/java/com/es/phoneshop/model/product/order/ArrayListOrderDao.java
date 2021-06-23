package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class ArrayListOrderDao implements OrderDao {
    private long orderId;
    private List<Order> orderList;

    private static volatile OrderDao instance;

    public static OrderDao getInstance() {
        OrderDao localInstance = instance;
        if (localInstance == null) {
            synchronized (OrderDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArrayListOrderDao();
                }
            }
        }
        return localInstance;
    }

    private ArrayListOrderDao() {
        this.orderList = new ArrayList<>();
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        if (id == null)
            throw new ProductNotFoundException(id);
        return orderList.stream()
                .filter(order -> id.equals(order.getId()))
                .findAny()
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public void saveOrder(Order order) throws OrderNotFoundException {
        if (order.getId() != null) {
            orderList.remove(getOrder(orderId));
        } else {
            order.setId(orderId++);
        }
        orderList.add(order);
    }

    @Override
    public Order getOrderBySecureId(String secureOrderId) {
        return orderList.stream()
                .filter(order -> secureOrderId.equals(order.getSecureId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }
}
