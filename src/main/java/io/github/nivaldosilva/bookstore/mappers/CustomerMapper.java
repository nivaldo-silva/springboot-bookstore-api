package io.github.nivaldosilva.bookstore.mappers;

import java.util.List;
import java.util.stream.Collectors;
import io.github.nivaldosilva.bookstore.dtos.request.CustomerRequest;
import io.github.nivaldosilva.bookstore.dtos.response.CustomerResponse;
import io.github.nivaldosilva.bookstore.dtos.response.OrderResponse;
import io.github.nivaldosilva.bookstore.entities.Customer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerMapper {

        public static Customer toEntity(CustomerRequest request) {
                return Customer.builder()
                                .id(request.id())
                                .fullName(request.fullName())
                                .email(request.email())
                                .password(request.password())
                                .build();
        }

        public static CustomerResponse toResponse(Customer customer) {
                List<OrderResponse> orders = null;
                if (customer.getOrders() != null) {
                        orders = customer.getOrders().stream()
                                        .map(OrderMapper::toResponse) 
                                        .collect(Collectors.toList());
                }

                return CustomerResponse.builder()
                                .id(customer.getId())
                                .fullName(customer.getFullName())
                                .email(customer.getEmail())
                                .orders(orders)
                                .createdAt(customer.getCreatedAt())
                                .updatedAt(customer.getUpdatedAt())
                                .build();
        }

}
