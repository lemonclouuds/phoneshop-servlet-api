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

    @Override
    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator;
        if (SortField.description == sortField) {
            comparator = Comparator.comparing(Product::getDescription);
        } else {
            comparator = Comparator.comparing(Product::getPrice);
        }
        if (SortOrder.desc == sortOrder) {
            comparator = comparator.reversed();
        }

        List<Product> result = products.stream()
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
            List<PriceHistory> prices = new ArrayList<>();
            if (products.removeIf(product1 -> isProductSuitableForDeleting(product, product1, prices))) {
                product.getPriceHistoryList().addAll(prices);
            }
        } else {
            product.setId(currId++);
        }
        products.add(product);
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

    private boolean isProductMatchingQuery(Product product, String query) {
        if (query == null || query.isEmpty()) {
            return true;
        }
        Stream<String> words = Arrays.stream(query.trim().split("\\s+"));

        return words.anyMatch(word ->Arrays.asList(product.getDescription().split(" ")).contains(word));
    }

    private long countEntrance(Product product, String query) {
        if (query == null || query.isEmpty()) {
            return 0;
        }
        List<String> words = new ArrayList<>(Arrays.asList(query.trim().split("\\s+")));
        Stream<String> description = Arrays.stream(product.getDescription().split("\\s+"));

        return description.filter(words::contains).count();
    }

    private float entranceMatchesDescription(Product product, long entrance) {
        Stream<String> description = Arrays.stream(product.getDescription().split("\\s+"));

        return (float) entrance / description.count();
    }
}
