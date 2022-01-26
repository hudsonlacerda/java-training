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

import digital.osf.product.model.Product;
import digital.osf.product.request.ProductRequestBody;
import digital.osf.product.request.ProductRequestParams;
import digital.osf.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> findAll(ProductRequestParams params, Pageable pageable) {
        Page<Product> products = this.productService.findAll(params.toSpec(), pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Integer id) {
        Product product = this.productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid ProductRequestBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.create(requestBody));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") Integer id,
            @RequestBody @Valid ProductRequestBody requestBody) {
        return ResponseEntity.ok().body(this.productService.update(id, requestBody));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        this.productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
