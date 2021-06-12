package com.es.phoneshop.model.product;

import com.es.phoneshop.data.DataFiller;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    Currency usd = Currency.getInstance("USD");

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        new DataFiller().fillSampleProducts().forEach(product -> productDao.save(product));
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);
        assertNotNull(product.getId());
        Product result = productDao.getProduct(product.getId());

        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test
    public void testDeleteProduct() throws ProductNotFoundException {
        Product product = new Product("test-product", "Specially for tests", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);
        productDao.delete(product.getId());

        assertFalse(productDao.findProducts().contains(product));
    }

    @Test
    public void testSaveNewPriceHistory() {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);

        Product newProduct = new Product("test-product", "Apple iPhone", new BigDecimal(150), usd, 150, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        newProduct.setId(product.getId());
        productDao.save(newProduct);

        assertEquals(new BigDecimal(150), productDao.getProduct(newProduct.getId()).getPriceHistoryList().get(0).getPrice());
        assertEquals(new BigDecimal(100), productDao.getProduct(newProduct.getId()).getPriceHistoryList().get(1).getPrice());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetNonExistentProduct() throws ProductNotFoundException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);
        productDao.delete(product.getId());
        productDao.getProduct(product.getId());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteNonExistentProduct() throws ProductNotFoundException {
        Product product = new Product("test-product", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product);
        productDao.delete(product.getId());
        productDao.delete(product.getId());
    }

    @Test
    public void testAscDescriptionSort() {
        Product product1 = new Product("product1", "Test", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product2 = new Product("product2", "Test A", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product3 = new Product("product3", "Test A 2", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        String query = "Test A 2";
        List<Product> expected = Arrays.asList(productDao.getProduct(product1.getId()),
                productDao.getProduct(product2.getId()),
                productDao.getProduct(product3.getId()));

        assertEquals(expected, productDao.findProducts(query, SortField.DESCRIPTION, SortOrder.ASC));
    }

    @Test
    public void testDescDescriptionSorting() {
        Product product1 = new Product("product1", "Test", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product2 = new Product("product2", "Test A", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product3 = new Product("product3", "Test A 2", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        String query = "Test A 2";
        List<Product> expected = Arrays.asList(productDao.getProduct(product3.getId()),
                productDao.getProduct(product2.getId()),
                productDao.getProduct(product1.getId()));

        assertEquals(expected, productDao.findProducts(query, SortField.DESCRIPTION, SortOrder.DESC));
    }

    @Test
    public void testAscPriceSort() {
        Product product1 = new Product("product1", "Test", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product2 = new Product("product2", "Test A", new BigDecimal(210), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product3 = new Product("product3", "Test A 2", new BigDecimal(220), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        String query = "Test";
        List<Product> expected = Arrays.asList(productDao.getProduct(product1.getId()),
                productDao.getProduct(product2.getId()),
                productDao.getProduct(product3.getId()));

        assertEquals(expected, productDao.findProducts(query, SortField.PRICE, SortOrder.ASC));
    }

    @Test
    public void testDescPriceSort() {
        Product product4 = new Product("product4", "Test", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product5 = new Product("product5", "Test A", new BigDecimal(210), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product6 = new Product("product6", "Test A 2", new BigDecimal(220), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product4);
        productDao.save(product5);
        productDao.save(product6);
        String query = "Test";
        List<Product> expected = Arrays.asList(productDao.getProduct(product6.getId()),
                productDao.getProduct(product5.getId()),
                productDao.getProduct(product4.getId()));

        assertEquals(expected, productDao.findProducts(query, SortField.PRICE, SortOrder.DESC));
    }

    @Test
    public void testOneWordQuerySort() {
        Product product4 = new Product("product4", "Test", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product5 = new Product("product5", "Test A", new BigDecimal(210), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product6 = new Product("product6", "Test A 2", new BigDecimal(220), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product4);
        productDao.save(product5);
        productDao.save(product6);
        String query = "Test";
        List<Product> expected = Arrays.asList(productDao.getProduct(product4.getId()),
                productDao.getProduct(product5.getId()),
                productDao.getProduct(product6.getId()));

        assertEquals(expected, productDao.findProducts(query));
    }

    @Test
    public void testTwoWordQuerySort1() {
        Product product4 = new Product("product4", "Test", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product5 = new Product("product5", "Test A", new BigDecimal(210), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        Product product6 = new Product("product6", "Test A 2", new BigDecimal(220), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg");
        productDao.save(product4);
        productDao.save(product5);
        productDao.save(product6);
        String query = "Test A";
        List<Product> expected = Arrays.asList(productDao.getProduct(product5.getId()),
                productDao.getProduct(product6.getId()),
                productDao.getProduct(product4.getId()));

        assertEquals(expected, productDao.findProducts(query));
    }
}
