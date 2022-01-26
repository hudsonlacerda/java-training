package digital.osf.product.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import digital.osf.product.exception.BadRequestException;
import digital.osf.product.model.Customer;
import digital.osf.product.repository.CustomerRepository;
import digital.osf.product.request.CustomerRequestBody;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Page<Customer> findAll(Specification<Customer> spec, Pageable pageable) {
        return this.customerRepository.findAll(spec, pageable);
    }

    public Customer findById(Integer id) {
        return this.customerRepository.findById(id).orElseThrow(() -> new BadRequestException("customer not found"));
    }

    public Customer create(@Valid CustomerRequestBody requestBody) {
        Customer customer = requestBody.convert();

        customer = this.customerRepository.save(customer);

        return customer;
    }

    public Customer update(Integer id, @Valid CustomerRequestBody requestBody) {
        this.findById(id);

        Customer newCustomer = requestBody.convert();
        newCustomer.setId(id);

        this.customerRepository.save(newCustomer);

        return newCustomer;
    }

    public void delete(Integer id) {
        this.customerRepository.delete(this.findById(id));
    }
}
