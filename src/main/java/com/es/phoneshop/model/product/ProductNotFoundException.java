package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException {
    Long id;

    public ProductNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
