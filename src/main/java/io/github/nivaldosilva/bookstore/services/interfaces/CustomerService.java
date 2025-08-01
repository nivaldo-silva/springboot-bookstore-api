package io.github.nivaldosilva.bookstore.services.interfaces;

import java.util.List;
import java.util.UUID;
import io.github.nivaldosilva.bookstore.dtos.request.CustomerRequest;
import io.github.nivaldosilva.bookstore.dtos.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse registerCustomer(CustomerRequest request);

    CustomerResponse findCustomerById(UUID id);

    List<CustomerResponse> findAllCustomers();

    CustomerResponse updateCustomer(UUID id, CustomerRequest request);

    void deleteCustomer(UUID id);
}
