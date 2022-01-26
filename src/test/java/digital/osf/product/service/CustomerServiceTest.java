package digital.osf.product.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import digital.osf.product.model.Customer;
import digital.osf.product.repository.CustomerRepository;
import digital.osf.product.request.CustomerRequestBody;
import digital.osf.product.request.CustomerRequestParams;
import digital.osf.product.util.CustomerUtil;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository CustomerRepository;

    @InjectMocks
    private CustomerService CustomerService;

    @BeforeEach
    void setup() {
        PageImpl<Customer> Customers = new PageImpl<>(CustomerUtil.savedCustomers());
        Mockito.when(this.CustomerRepository.save(ArgumentMatchers.any(Customer.class))).thenReturn(CustomerUtil.savedCustomer());
        Mockito.doNothing().when(this.CustomerRepository).delete(ArgumentMatchers.any(Customer.class));
        Mockito.when(this.CustomerRepository.findById(CustomerUtil.savedCustomer().getId())).thenReturn(Optional.of(CustomerUtil.savedCustomer()));
        Mockito.when(this.CustomerRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.any(Pageable.class))).thenReturn(Customers);
    }

    @Test
    public void findAll_ReturnPagebleCustomers_WhenSuccessful() {
        Page<Customer> customer = this.CustomerService.findAll(new CustomerRequestParams().toSpec(), PageRequest.of(1, 1));

        Assertions.assertThat(customer).isNotNull();
        Assertions.assertThat(customer).isNotEmpty();
        Assertions.assertThat(customer.getNumberOfElements()).isEqualTo(CustomerUtil.savedCustomers().size());
    }

    @Test
    public void findById_ReturnCustomerFromSpecificId_WhenSuccessful() {
        Customer customer = this.CustomerService.findById(CustomerUtil.savedCustomer().getId());

        Assertions.assertThat(customer).isNotNull();
        Assertions.assertThat(customer).isEqualTo(CustomerUtil.savedCustomer());
    }

    @Test
    public void create_PersistAndReturnNewCustomer_WhenSuccessful() {
        CustomerRequestBody requestBody = CustomerUtil.customerRequestBody();
        Customer createdCustomer = this.CustomerService.create(requestBody);

        Assertions.assertThat(createdCustomer).isNotNull();
        Assertions.assertThat(createdCustomer.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingCustomer_WhenSuccessful() {
        CustomerRequestBody requestBody = CustomerUtil.customerRequestBody();
        Customer updatedCustomer = this.CustomerService.update(CustomerUtil.savedCustomer().getId(), requestBody);

        Assertions.assertThat(updatedCustomer).isNotNull();
        Assertions.assertThat(updatedCustomer.getId()).isNotNull();
    }

    @Test
    public void delete_DeleteCustomerById_WhenSuccessful() {
        Assertions.assertThatCode(() -> this.CustomerService.delete(CustomerUtil.savedCustomer().getId())).doesNotThrowAnyException();
    }
}
