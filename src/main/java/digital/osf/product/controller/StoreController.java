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

import digital.osf.product.model.Store;
import digital.osf.product.request.StoreRequestBody;
import digital.osf.product.request.StoreRequestParams;
import digital.osf.product.service.StoreService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<Page<Store>> findAll(StoreRequestParams params, Pageable pageable) {
        Page<Store> stores = this.storeService.findAll(params.toSpec(), pageable);
        return ResponseEntity.ok().body(stores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> findById(@PathVariable("id") Integer id) {
        Store store = this.storeService.findById(id);
        return ResponseEntity.ok().body(store);
    }

    @PostMapping
    public ResponseEntity<Store> create(@RequestBody @Valid StoreRequestBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.storeService.create(requestBody));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> update(@PathVariable("id") Integer id,
            @RequestBody @Valid StoreRequestBody requestBody) {
        return ResponseEntity.ok().body(this.storeService.update(id, requestBody));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        this.storeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

