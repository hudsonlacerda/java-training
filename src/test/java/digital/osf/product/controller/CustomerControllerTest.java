package digital.osf.product.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import digital.osf.product.exception.BadRequestException;
import digital.osf.product.model.Customer;
import digital.osf.product.request.CustomerRequestBody;
import digital.osf.product.request.CustomerRequestParams;
import digital.osf.product.service.CustomerService;
import digital.osf.product.util.CustomerUtil;

@ExtendWith(SpringExtension.class)
public class CustomerControllerTest {
    
    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setup() {
            PageImpl<Customer> customers = new PageImpl<>(CustomerUtil.savedCustomers());
            Mockito.when(this.customerService.findById(CustomerUtil.savedCustomer().getId())).thenReturn(CustomerUtil.savedCustomer());
            Mockito.when(this.customerService.findAll(ArgumentMatchers.any(), ArgumentMatchers.any(Pageable.class))).thenReturn(customers);
            Mockito.when(this.customerService.create(ArgumentMatchers.any(CustomerRequestBody.class))).thenReturn(CustomerUtil.savedCustomer());
            Mockito.when(this.customerService.update(ArgumentMatchers.eq(CustomerUtil.savedCustomer().getId()), ArgumentMatchers.any(CustomerRequestBody.class))).thenReturn(CustomerUtil.savedCustomer());
            Mockito.when(this.customerService.update( AdditionalMatchers.not(ArgumentMatchers.eq(CustomerUtil.savedCustomer().getId())), ArgumentMatchers.any(CustomerRequestBody.class))).thenThrow(new BadRequestException("customer not found"));
            Mockito.doNothing().when(this.customerService).delete(ArgumentMatchers.anyInt());
    }

    @Test
    public void findAll_ReturncustomerFromSpecificCity_WhenSuccessful() {
            ResponseEntity<Page<Customer>> response = this.customerController.findAll(CustomerRequestParams.builder().city("city").build(), PageRequest.of(0, 1));

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<Customer> customers = response.getBody().getContent();

            Assertions.assertThat(customers).isNotNull();
            Assertions.assertThat(customers).isNotEmpty();
            Assertions.assertThat(customers.size()).isEqualTo(CustomerUtil.savedCustomers().size());
    }

    @Test
    public void findAll_ReturnPageblecustomers_WhenSuccessful() {
            ResponseEntity<Page<Customer>> response = this.customerController.findAll(new CustomerRequestParams(), PageRequest.of(1, 1));

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Page<Customer> customers = response.getBody();

            Assertions.assertThat(customers).isNotNull();
            Assertions.assertThat(customers).isNotEmpty();
            Assertions.assertThat(customers.getNumberOfElements()).isEqualTo(CustomerUtil.savedCustomers().size());
    }

    @Test
    public void findById_ReturncustomerFromSpecificId_WhenSuccessful() {
            ResponseEntity<Customer> response = this.customerController.findById(CustomerUtil.savedCustomer().getId());

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Customer customer = response.getBody();

            Assertions.assertThat(customer).isNotNull();
            Assertions.assertThat(customer).isEqualTo(CustomerUtil.savedCustomer());
    }

    @Test
    public void create_PersistAndReturnNewcustomer_WhenSuccessful() {
            CustomerRequestBody requestBody = CustomerUtil.customerRequestBody();
            ResponseEntity<Customer> response = this.customerController.create(requestBody);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            Customer createdcustomer = response.getBody();

            Assertions.assertThat(createdcustomer).isNotNull();
            Assertions.assertThat(createdcustomer.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingcustomer_WhenSuccessful() {
            CustomerRequestBody requestBody = CustomerUtil.customerRequestBody();
            ResponseEntity<Customer> response = this.customerController.update(CustomerUtil.savedCustomer().getId(),
                            requestBody);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            Customer createdcustomer = response.getBody();

            Assertions.assertThat(createdcustomer).isNotNull();
            Assertions.assertThat(createdcustomer.getId()).isNotNull();
    }

    @Test
    public void update_ReturnBadRequest_WhenNotexists() {
            CustomerRequestBody requestBody = CustomerUtil.customerRequestBody();
            Exception badRequest = assertThrows(Exception.class, () -> this.customerController
                            .update(CustomerUtil.savedCustomer().getId() + 1, requestBody));
            Assertions.assertThat(badRequest.getClass()).isEqualTo(BadRequestException.class);
    }

    @Test
    public void delete_DeletecustomerById_WhenSuccessful() {
            Assertions.assertThatCode(() -> this.customerController.delete(CustomerUtil.savedCustomer().getId()))
                            .doesNotThrowAnyException();
            ResponseEntity<Void> response = this.customerController.delete(CustomerUtil.savedCustomer().getId());
            Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    }
}