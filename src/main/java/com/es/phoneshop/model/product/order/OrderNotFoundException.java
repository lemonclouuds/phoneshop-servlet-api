package com.es.phoneshop.model.product.order;

public class OrderNotFoundException extends RuntimeException {
    Long id;

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
