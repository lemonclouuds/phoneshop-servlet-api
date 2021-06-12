package com.es.phoneshop.model.product.recentlyViewed;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.Deque;

public class DefaultRecentlyViewedService implements RecentlyViewedService {
    private static final String VIEW_HISTORY_LIST_SESSION_ATTRIBUTE = DefaultRecentlyViewedService.class.getName() + ".list";
    private final int PRODUCT_AMOUNT = 3;
    private ProductDao productDao;

    private static volatile RecentlyViewedService instance;

    private DefaultRecentlyViewedService() {
        productDao = ArrayListProductDao.getInstance();
    }


    public static RecentlyViewedService getInstance() {
        RecentlyViewedService localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultRecentlyViewedService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public synchronized RecentlyViewedList getRecentlyViewedList(HttpServletRequest request) {
        RecentlyViewedList list = (RecentlyViewedList) request.getSession().getAttribute(VIEW_HISTORY_LIST_SESSION_ATTRIBUTE);
        if (list == null) {
            request.getSession().setAttribute(VIEW_HISTORY_LIST_SESSION_ATTRIBUTE, list = new RecentlyViewedList());
        }
        return list;
    }

    @Override
    public Deque<Product> getRecentlyViewedListItems(RecentlyViewedList list) {
        return list.getLastViewed();
    }

    @Override
    public synchronized void add(RecentlyViewedList list, Long productId) {
        Product product = productDao.getProduct(productId);
        list.addProduct(product, PRODUCT_AMOUNT);
    }
}
