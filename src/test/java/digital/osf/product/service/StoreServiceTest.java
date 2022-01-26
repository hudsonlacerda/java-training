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

import digital.osf.product.model.Store;
import digital.osf.product.repository.StoreRepository;
import digital.osf.product.request.StoreRequestBody;
import digital.osf.product.request.StoreRequestParams;
import digital.osf.product.util.StoreUtil;

@ExtendWith(SpringExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setup() {
        PageImpl<Store> stores = new PageImpl<>(StoreUtil.savedStores());
        Mockito.when(this.storeRepository.save(ArgumentMatchers.any(Store.class))).thenReturn(StoreUtil.savedStore());
        Mockito.doNothing().when(this.storeRepository).delete(ArgumentMatchers.any(Store.class));
        Mockito.when(this.storeRepository.findById(StoreUtil.savedStore().getId())).thenReturn(Optional.of(StoreUtil.savedStore()));
        Mockito.when(this.storeRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.any(Pageable.class))).thenReturn(stores);
    }

    @Test
    public void findAll_ReturnPagebleStores_WhenSuccessful() {
        Page<Store> stores = this.storeService.findAll(new StoreRequestParams().toSpec(), PageRequest.of(1, 1));

        Assertions.assertThat(stores).isNotNull();
        Assertions.assertThat(stores).isNotEmpty();
        Assertions.assertThat(stores.getNumberOfElements()).isEqualTo(StoreUtil.savedStores().size());
    }

    @Test
    public void findById_ReturnStoreFromSpecificId_WhenSuccessful() {
        Store store = this.storeService.findById(StoreUtil.savedStore().getId());

        Assertions.assertThat(store).isNotNull();
        Assertions.assertThat(store).isEqualTo(StoreUtil.savedStore());
    }

    @Test
    public void create_PersistAndReturnNewStore_WhenSuccessful() {
        StoreRequestBody requestBody = StoreUtil.storeRequestBody();
        Store createdstore = this.storeService.create(requestBody);

        Assertions.assertThat(createdstore).isNotNull();
        Assertions.assertThat(createdstore.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingStore_WhenSuccessful() {
        StoreRequestBody requestBody = StoreUtil.storeRequestBody();
        Store updatedstore = this.storeService.update(StoreUtil.savedStore().getId(), requestBody);

        Assertions.assertThat(updatedstore).isNotNull();
        Assertions.assertThat(updatedstore.getId()).isNotNull();
    }

    @Test
    public void delete_DeleteStoreById_WhenSuccessful() {
        Assertions.assertThatCode(() -> this.storeService.delete(StoreUtil.savedStore().getId())).doesNotThrowAnyException();
    }
}
