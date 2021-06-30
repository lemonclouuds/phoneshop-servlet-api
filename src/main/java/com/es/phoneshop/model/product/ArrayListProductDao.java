package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao implements ProductDao {
    private long currId;
    private List<Product> products;

    private static volatile ProductDao instance;

    public static ProductDao getInstance() {
        ProductDao localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArrayListProductDao();
                }
            }
        }
        return localInstance;
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

    @Override
    public List<Product> findProducts(String description, String wordOpt, BigDecimal minPrice, BigDecimal maxPrice) {
        boolean isQueryEmpty = Stream.of(description, wordOpt, minPrice, maxPrice).allMatch(Objects::isNull);
        if (isQueryEmpty) {
            return new ArrayList<>();
        }

        if (description == null || description.isEmpty()) {
            return products.stream()
                    .filter(this::isProductNotNull)
                    .filter(this::isPriceNotNull)
                    .filter(this::isStockPositive)
                    .filter(product -> product.getPrice().compareTo(minPrice) >= 0)
                    .filter(product -> product.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        }

        WordOptions wordOptions = WordOptions.valueOf(wordOpt.toUpperCase().replace(" ", "_"));
        Predicate<Product> match;
        String[] descriptionWords = description.trim().toLowerCase().split(" ");

        match = getMatch(wordOptions, descriptionWords);

        return products.stream()
                .filter(this::isProductNotNull)
                .filter(this::isPriceNotNull)
                .filter(this::isStockPositive)
                .filter(match)
                .filter(minPrice != null ? product -> product.getPrice().compareTo(minPrice) >= 0 : product -> true)
                .filter(maxPrice != null ? product -> product.getPrice().compareTo(maxPrice) <= 0 : product -> true)
                .collect(Collectors.toList());
    }

    private Predicate<Product> getMatch(WordOptions wordOptions, String[] descriptionWords){
        if (wordOptions == WordOptions.ANY_WORD) {
            return product -> Arrays.stream(descriptionWords)
                    .anyMatch(descriptionWord -> product.getDescription()
                            .toLowerCase()
                            .contains(descriptionWord));
        } else {
            return product -> Arrays.stream(descriptionWords)
                    .allMatch(descriptionWord -> product.getDescription()
                            .toLowerCase()
                            .contains(descriptionWord));
        }
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

        return Arrays.stream(product.getDescription().split("\\s+")).filter(words::contains).count();
    }

    private float entranceMatchesDescription(Product product, long entrance) {
        Stream<String> description = Arrays.stream(product.getDescription().split("\\s+"));

        return (float) entrance / description.count();
    }
}
