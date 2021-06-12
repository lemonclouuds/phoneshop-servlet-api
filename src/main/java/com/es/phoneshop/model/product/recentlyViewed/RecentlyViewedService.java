package com.es.phoneshop.model.product.recentlyViewed;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Deque;

public interface RecentlyViewedService {
    RecentlyViewedList getRecentlyViewedList(HttpServletRequest request);
    Deque<Product> getRecentlyViewedListItems(RecentlyViewedList list);
    void add(RecentlyViewedList list, Long productId);
}
