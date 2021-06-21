package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.ProductNotFoundException;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;
    void saveOrder(Order order) throws OrderNotFoundException;
}
