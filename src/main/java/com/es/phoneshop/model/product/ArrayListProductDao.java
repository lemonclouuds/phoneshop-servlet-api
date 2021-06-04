package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (sortField != null && SortField.description == sortField) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (sortOrder != null && SortOrder.desc == sortOrder) {
            comparator = comparator.reversed();
        }

        return products.stream()
                .filter(this::isProductNotNull)
                .filter(this::isPriceNotNull)
                .filter(this::isStockPositive)
                .filter(product -> query == null || query.isEmpty() || product.getDescription().contains(query))
                .sorted(comparator)
                .collect(Collectors.toList());
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
            List<PriceHistory> prices = new ArrayList<>();
            if (products.removeIf(product1 -> isProductSuitableForDeleting(product, product1, prices))) {
                product.getPriceHistoryList().addAll(prices);
            }
            products.add(product);
            return;
        } else {
            product.setId(currId++);
            products.add(product);
        }
    }

    @Override
    public synchronized void delete(Long id) throws ProductNotFoundException {
        if (id == null) {
            throw new ProductNotFoundException(id);
        }
        if (!products.removeIf(product -> id.equals(product.getId()))) {
            throw new ProductNotFoundException(id);
        } else {
            currId--;
        }
    }

    private boolean isProductPriceUpdated(Product newProduct, Product currentProduct) {
        return newProduct.getId().equals(currentProduct.getId()) && !newProduct.getPrice().equals(currentProduct.getPrice());
    }

    private boolean isProductSuitableForDeleting(Product newProduct, Product currentProduct, List<PriceHistory> priceHistory) {
        if (isProductPriceUpdated(newProduct, currentProduct)) {
            priceHistory.addAll(currentProduct.getPriceHistoryList());
        }
        return newProduct.getId().equals(currentProduct.getId());
    }
}
