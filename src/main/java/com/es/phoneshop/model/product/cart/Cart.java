package com.es.phoneshop.model.product.cart;

import java.util.List;

public class Cart {
    List<CartItem> items;

    public Cart(List<CartItem> items) {
        this.items = items;
    }

    public Cart() {

    }

    public List<CartItem> getItems() {
        return items;
    }
}
