package digital.osf.product.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import digital.osf.product.model.Customer;
import digital.osf.product.repository.CustomerRepository;
import digital.osf.product.request.CustomerRequestBody;
import digital.osf.product.util.CustomerUtil;
import digital.osf.product.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CustomerResourceIT {
    @Autowired
    @Qualifier("restTemplate")
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Lazy
    @TestConfiguration
    static class Config {
        @Bean(name = "restTemplate")
        public TestRestTemplate restTemplateCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port);
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    public void findByCategory_ReturnCustomerFromSpecificCategory_WhenSuccessful() {
        this.customerRepository.saveAll(CustomerUtil.listCustomersToPersist());

        ResponseEntity<PageableResponse<Customer>> response = restTemplate.exchange("/customers?city=city1", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Customer>>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<Customer> Customers = response.getBody();

        Assertions.assertThat(Customers).isNotNull();
        Assertions.assertThat(Customers).allSatisfy(s -> s.getCity().equals("city1"));
    }

    @Test
    public void findAll_ReturnPagebleCustomers_WhenSuccessful() {
        this.customerRepository.saveAll(CustomerUtil.listCustomersToPersist());

        ResponseEntity<PageableResponse<Customer>> response = this.restTemplate.exchange("/customers", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Customer>>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Page<Customer> Customers = response.getBody();

        Assertions.assertThat(Customers).isNotNull();
        Assertions.assertThat(Customers).isNotEmpty();
        Assertions.assertThat(Customers.getNumberOfElements()).isEqualTo(CustomerUtil.savedCustomers().size());
    }

    @Test
    public void findById_ReturnCustomerFromSpecificId_WhenSuccessful() {
        Customer savedCustomer = this.customerRepository.save(CustomerUtil.customerToPersist());

        ResponseEntity<Customer> response = this.restTemplate.getForEntity("/customers/{id}", Customer.class,
                savedCustomer.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Customer Customer = response.getBody();

        Assertions.assertThat(Customer).isNotNull();
        Assertions.assertThat(Customer).isEqualTo(savedCustomer);
    }

    @Test
    public void create_PersistAndReturnNewCustomer_WhenSuccessful() {
        CustomerRequestBody requestBody = CustomerUtil.customerRequestBody();

        ResponseEntity<Customer> response = restTemplate.exchange("/customers", HttpMethod.POST,
                new HttpEntity<CustomerRequestBody>(requestBody),
                new ParameterizedTypeReference<Customer>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Customer createdCustomer = response.getBody();

        Assertions.assertThat(createdCustomer).isNotNull();
        Assertions.assertThat(createdCustomer.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingCustomer_WhenSuccessful() {
        CustomerRequestBody requestBody = CustomerUtil.customerRequestBody();

        ResponseEntity<Customer> postResponse = restTemplate.exchange("/customers", HttpMethod.POST,
                new HttpEntity<CustomerRequestBody>(requestBody),
                new ParameterizedTypeReference<Customer>() {
                });

        requestBody.setPhone("8888-8888");
        requestBody.setEmail("Customer@osf.digital");

        ResponseEntity<Customer> putResponse = restTemplate.exchange("/customers/" + postResponse.getBody().getId(),
                HttpMethod.PUT,
                new HttpEntity<CustomerRequestBody>(requestBody),
                new ParameterizedTypeReference<Customer>() {
                });

        Assertions.assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(postResponse.getBody()).isNotEqualTo(putResponse.getBody());
    }

    @Test
    public void delete_DeleteCustomerById_WhenSuccessful() {
        Customer savedCustomer = this.customerRepository.save(CustomerUtil.customerToPersist());

        ResponseEntity<Void> response = this.restTemplate.exchange("/customers/{id}", HttpMethod.DELETE, null,
                Void.class, savedCustomer.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<Customer> pOptional = this.customerRepository.findById(savedCustomer.getId());

        Assertions.assertThat(pOptional.isEmpty()).isTrue();
    }
}