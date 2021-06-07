package com.es.phoneshop.model.product.viewHistory;

import javax.servlet.http.HttpServletRequest;

public interface ViewHistoryService {
    ViewHistoryList getViewHistoryList(HttpServletRequest request);
    void add(ViewHistoryList list, Long productId);
}
