package com.es.phoneshop.model.product.recentlyViewed;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class RecentlyViewedList {
    private Deque<Product> lastViewed;

    public RecentlyViewedList() {
        this.lastViewed = new ArrayDeque<>();
    }

    public void addProduct(Product product, int amount) {
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
        if (lastViewed.size() == amount) {
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
