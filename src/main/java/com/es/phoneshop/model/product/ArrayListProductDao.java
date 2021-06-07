package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {
    private long currId;
    private List<Product> products;

    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    @Override
    public synchronized Product getProduct(Long id) throws ProductNotFoundException {
        if (id == null)
            throw new ProductNotFoundException(id);
        return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public synchronized List<Product> findProducts() {
        return products.stream()
                .filter(this::isProductNotNull)
                .filter(this::isPriceNotNull)
                .filter(this::isStockPositive)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Product> findProducts(String query) {
        return products.stream()
                .filter(this::isProductNotNull)
                .filter(this::isPriceNotNull)
                .filter(this::isStockPositive)
                .filter(product -> isProductMatchingQuery(product, query))
                .sorted((product1, product2) -> {
                    long firstEntrance = countEntrance(product1, query);
                    long secondEntrance = countEntrance(product2, query);
                    if (firstEntrance == secondEntrance) {
                        return Float.compare(entranceMatchesDescription(product2, secondEntrance),
                                entranceMatchesDescription(product1, firstEntrance));
                    } else {
                        return Long.compare(secondEntrance, firstEntrance);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator;
        if (SortField.DESCRIPTION == sortField) {
            comparator = Comparator.comparing(Product::getDescription);
        } else {
            comparator = Comparator.comparing(Product::getPrice);
        }
        if (SortOrder.DESC == sortOrder) {
            comparator = comparator.reversed();
        }

        List<Product> result = findProducts(query);

        if (sortField != null) {
            result = result.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
        return result;
    }

    private boolean isProductNotNull(Product product) {
        return product != null;
    }

    private boolean isPriceNotNull(Product product) {
        return product.getPrice() != null;
    }

    private boolean isStockPositive(Product product) {
        return product.getStock() > 0;
    }

    @Override
    public synchronized void save(Product product) {
        if (product.getId() != null) {
            if (products.removeIf(product1 -> product1.getId().equals(product.getId()))) {
            } else {
                product.setId(currId++);
            }
            products.add(product);

        }
        products.add(product);
    }
}
