package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.data.DataFiller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class DemoDataServletContextListener implements ServletContextListener {
    private ProductDao productDao;

    public DemoDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if (Boolean.parseBoolean(servletContextEvent.getServletContext().getInitParameter("insertDemoData"))) {
            fillSampleProducts().forEach(product -> {
                productDao.save(product);
            });
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public List<Product> fillSampleProducts() {
        return new DataFiller().fillSampleProducts();
    }

}
