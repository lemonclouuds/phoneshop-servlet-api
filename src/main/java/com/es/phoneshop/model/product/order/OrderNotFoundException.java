package com.es.phoneshop.model.product.order;

public class OrderNotFoundException extends RuntimeException {
    private Long id;

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(Long id) {
        super("Order (id = " + id +") not found");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
