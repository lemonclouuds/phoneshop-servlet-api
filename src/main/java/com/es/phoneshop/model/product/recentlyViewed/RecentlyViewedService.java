package com.es.phoneshop.model.product.recentlyViewed;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedService {
    RecentlyViewedList getRecentlyViewedList(HttpServletRequest request);
    void add(RecentlyViewedList list, Long productId);
}
