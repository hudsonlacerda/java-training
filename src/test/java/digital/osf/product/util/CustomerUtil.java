package digital.osf.product.util;

import java.util.List;

import digital.osf.product.model.Customer;
import digital.osf.product.request.CustomerRequestBody;

public class CustomerUtil {

    public static CustomerRequestBody customerRequestBody() {
        return CustomerRequestBody.builder()
                .firstName("firstName")
                .lastName("lastName")
                .phone("phone")
                .email("email")
                .street("street")
                .city("city")
                .state("state")
                .zipCode("12345")
                .build();
    }

    public static Customer customerToPersist() {
        return Customer.builder()
                .firstName("firstName")
                .lastName("lastName")
                .phone("phone")
                .email("email")
                .street("street")
                .city("city")
                .state("state")
                .zipCode("12345")
                .build();
    }

    public static Customer savedCustomer() {
        return Customer.builder()
                .id(1)
                .firstName("firstName")
                .lastName("lastName")
                .phone("phone")
                .email("email")
                .street("street")
                .city("city")
                .state("state")
                .zipCode("12345")
                .build();
    }

    public static List<Customer> listCustomersToPersist() {
        return List.of(customerToPersist());
    }

    public static List<Customer> savedCustomers() {
        return List.of(savedCustomer());
    }
}
