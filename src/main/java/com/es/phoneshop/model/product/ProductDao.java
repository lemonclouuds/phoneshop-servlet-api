package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    List<Product> findProducts();
    List<Product> findProducts(String query);
    List<Product> findProducts(String description, String wordOpt, BigDecimal minPrice, BigDecimal maxPrice);
    void save(Product product);
    void delete(Long id) throws ProductNotFoundException;
}
