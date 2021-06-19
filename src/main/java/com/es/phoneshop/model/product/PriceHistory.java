package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class PriceHistory implements Serializable {
    private LocalDate localDate;
    private BigDecimal price;

    public PriceHistory() {
    }

    public PriceHistory(BigDecimal price) {
        this.price = price;
        this.localDate = LocalDate.now();
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDate, price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass()!=obj.getClass()) return false;
        PriceHistory priceHistory = (PriceHistory) obj;
        return Objects.equals(localDate, priceHistory.localDate) && Objects.equals(price, priceHistory.price);
    }
}
