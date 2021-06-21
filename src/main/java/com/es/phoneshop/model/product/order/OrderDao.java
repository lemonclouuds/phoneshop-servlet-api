package com.es.phoneshop.model.product.order;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;
    Order getOrderBySecureId(String secureOrderId);
    void saveOrder(Order order) throws OrderNotFoundException;
}
