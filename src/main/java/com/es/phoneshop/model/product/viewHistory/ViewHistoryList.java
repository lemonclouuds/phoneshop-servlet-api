package com.es.phoneshop.model.product.viewHistory;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class ViewHistoryList {
    private Deque<Product> lastViewed;

    public ViewHistoryList() {
        this.lastViewed = new ArrayDeque<>();
    }

    public void addProduct(Product product) {
        if (lastViewed.contains(product)) {
            Product first = lastViewed.peek();
            if (first.equals(product)) {
                return;
            }
            Iterator<Product> iterator = lastViewed.iterator();
            while (iterator.hasNext()) {
                Product nextProduct = iterator.next();
                if (nextProduct.equals(product)) {
                    iterator.remove();
                    lastViewed.addFirst(product);
                    break;
                }
            }
            return;
        }
        if (lastViewed.size() == 3) {
            lastViewed.removeLast();
            lastViewed.addFirst(product);
            return;
        } else {
            lastViewed.addFirst(product);
        }
    }

    public Deque<Product> getLastViewed() {
        return lastViewed;
    }
}
