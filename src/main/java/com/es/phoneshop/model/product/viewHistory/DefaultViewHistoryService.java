package com.es.phoneshop.model.product.viewHistory;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;

public class DefaultViewHistoryService implements ViewHistoryService{
    private static final String VIEW_HISTORY_LIST_SESSION_ATTRIBUTE = DefaultViewHistoryService.class.getName() + ".list";
    private ProductDao productDao;

    private static DefaultViewHistoryService instance;

    private DefaultViewHistoryService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static synchronized DefaultViewHistoryService getInstance() {
        if (instance == null) {
            instance = new DefaultViewHistoryService();
        }
        return instance;
    }

    @Override
    public synchronized ViewHistoryList getViewHistoryList(HttpServletRequest request) {
        ViewHistoryList list = (ViewHistoryList) request.getSession().getAttribute(VIEW_HISTORY_LIST_SESSION_ATTRIBUTE);
        if (list == null) {
            request.getSession().setAttribute(VIEW_HISTORY_LIST_SESSION_ATTRIBUTE, list = new ViewHistoryList());
        }
        return list;
    }

    @Override
    public synchronized void add(ViewHistoryList list, Long productId) {
        Product product = productDao.getProduct(productId);
        list.addProduct(product);
    }
}
