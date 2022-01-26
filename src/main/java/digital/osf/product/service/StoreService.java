package digital.osf.product.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import digital.osf.product.exception.BadRequestException;
import digital.osf.product.model.Store;
import digital.osf.product.repository.StoreRepository;
import digital.osf.product.request.StoreRequestBody;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public Page<Store> findAll(Specification<Store> spec, Pageable pageable) {
        return this.storeRepository.findAll(spec, pageable);
    }

    public Store findById(Integer id) {
        return this.storeRepository.findById(id).orElseThrow(() -> new BadRequestException("store not found"));
    }

    public Store create(@Valid StoreRequestBody requestBody) {
        Store store = requestBody.convert();

        store = this.storeRepository.save(store);

        return store;
    }

    public Store update(Integer id, @Valid StoreRequestBody requestBody) {
        this.findById(id);

        Store newStore = requestBody.convert();
        newStore.setId(id);

        this.storeRepository.save(newStore);

        return newStore;
    }

    public void delete(Integer id) {
        this.storeRepository.delete(this.findById(id));
    }

}