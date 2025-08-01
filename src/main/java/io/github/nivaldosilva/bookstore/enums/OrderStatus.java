package io.github.nivaldosilva.bookstore.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED

}
