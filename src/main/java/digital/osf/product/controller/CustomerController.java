package digital.osf.product.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import digital.osf.product.model.Customer;
import digital.osf.product.request.CustomerRequestBody;
import digital.osf.product.request.CustomerRequestParams;
import digital.osf.product.service.CustomerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<Customer>> findAll(CustomerRequestParams params, Pageable pageable) {
        Page<Customer> customers = this.customerService.findAll(params.toSpec(), pageable);
        return ResponseEntity.ok().body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable("id") Integer id) {
        Customer customer = this.customerService.findById(id);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody @Valid CustomerRequestBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerService.create(requestBody));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable("id") Integer id,
            @RequestBody @Valid CustomerRequestBody requestBody) {
        return ResponseEntity.ok().body(this.customerService.update(id, requestBody));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        this.customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
