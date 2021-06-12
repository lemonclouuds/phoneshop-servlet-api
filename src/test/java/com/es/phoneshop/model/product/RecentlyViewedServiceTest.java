package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.recentlyViewed.DefaultRecentlyViewedService;
import com.es.phoneshop.model.product.recentlyViewed.RecentlyViewedList;
import com.es.phoneshop.model.product.recentlyViewed.RecentlyViewedService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

public class RecentlyViewedServiceTest {
    private ProductDao productDao;
    private RecentlyViewedService recentlyViewedService;
    Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        recentlyViewedService = DefaultRecentlyViewedService.getInstance();
    }

    @Test
    public void testAddProductToRecentlyViewedList() {
        Product product1 = new Product("product1", "Test", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product2 = new Product("product2", "Test A", new BigDecimal(210), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product3 = new Product("product3", "Test A 2", new BigDecimal(220), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);

        RecentlyViewedList list = new RecentlyViewedList();
        recentlyViewedService.add(list, product3.getId());
        recentlyViewedService.add(list, product2.getId());
        recentlyViewedService.add(list, product1.getId());

        long productId = 0L;
        for (Product product : recentlyViewedService.getRecentlyViewedListItems(list)) {
            assertEquals(product, productDao.getProduct(productId));
            productId++;
        }
    }
}