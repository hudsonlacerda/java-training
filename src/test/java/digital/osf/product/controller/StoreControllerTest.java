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
import digital.osf.product.model.Store;
import digital.osf.product.request.StoreRequestBody;
import digital.osf.product.request.StoreRequestParams;
import digital.osf.product.service.StoreService;
import digital.osf.product.util.StoreUtil;

@ExtendWith(SpringExtension.class)
public class StoreControllerTest {

    @InjectMocks
    private StoreController storeController;

    @Mock
    private StoreService storeService;

    @BeforeEach
    void setup() {
            PageImpl<Store> stores = new PageImpl<>(StoreUtil.savedStores());
            Mockito.when(this.storeService.findById(StoreUtil.savedStore().getId())).thenReturn(StoreUtil.savedStore());
            Mockito.when(this.storeService.findAll(ArgumentMatchers.any(), ArgumentMatchers.any(Pageable.class))).thenReturn(stores);
            Mockito.when(this.storeService.create(ArgumentMatchers.any(StoreRequestBody.class))).thenReturn(StoreUtil.savedStore());
            Mockito.when(this.storeService.update(ArgumentMatchers.eq(StoreUtil.savedStore().getId()), ArgumentMatchers.any(StoreRequestBody.class))).thenReturn(StoreUtil.savedStore());
            Mockito.when(this.storeService.update( AdditionalMatchers.not(ArgumentMatchers.eq(StoreUtil.savedStore().getId())), ArgumentMatchers.any(StoreRequestBody.class))).thenThrow(new BadRequestException("store not found"));
            Mockito.doNothing().when(this.storeService).delete(ArgumentMatchers.anyInt());
    }

    @Test
    public void findAll_ReturnStoreFromSpecificCity_WhenSuccessful() {
            ResponseEntity<Page<Store>> response = this.storeController.findAll(StoreRequestParams.builder().city("city").build(), PageRequest.of(0, 1));

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<Store> stores = response.getBody().getContent();

            Assertions.assertThat(stores).isNotNull();
            Assertions.assertThat(stores).isNotEmpty();
            Assertions.assertThat(stores.size()).isEqualTo(StoreUtil.savedStores().size());
    }

    @Test
    public void findAll_ReturnPagebleStores_WhenSuccessful() {
            ResponseEntity<Page<Store>> response = this.storeController.findAll(new StoreRequestParams(), PageRequest.of(1, 1));

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Page<Store> stores = response.getBody();

            Assertions.assertThat(stores).isNotNull();
            Assertions.assertThat(stores).isNotEmpty();
            Assertions.assertThat(stores.getNumberOfElements()).isEqualTo(StoreUtil.savedStores().size());
    }

    @Test
    public void findById_ReturnStoreFromSpecificId_WhenSuccessful() {
            ResponseEntity<Store> response = this.storeController.findById(StoreUtil.savedStore().getId());

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Store store = response.getBody();

            Assertions.assertThat(store).isNotNull();
            Assertions.assertThat(store).isEqualTo(StoreUtil.savedStore());
    }

    @Test
    public void create_PersistAndReturnNewStore_WhenSuccessful() {
            StoreRequestBody requestBody = StoreUtil.storeRequestBody();
            ResponseEntity<Store> response = this.storeController.create(requestBody);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            Store createdstore = response.getBody();

            Assertions.assertThat(createdstore).isNotNull();
            Assertions.assertThat(createdstore.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingStore_WhenSuccessful() {
            StoreRequestBody requestBody = StoreUtil.storeRequestBody();
            ResponseEntity<Store> response = this.storeController.update(StoreUtil.savedStore().getId(),
                            requestBody);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            Store createdstore = response.getBody();

            Assertions.assertThat(createdstore).isNotNull();
            Assertions.assertThat(createdstore.getId()).isNotNull();
    }

    @Test
    public void update_ReturnBadRequest_WhenNotexists() {
            StoreRequestBody requestBody = StoreUtil.storeRequestBody();
            Exception badRequest = assertThrows(Exception.class, () -> this.storeController
                            .update(StoreUtil.savedStore().getId() + 1, requestBody));
            Assertions.assertThat(badRequest.getClass()).isEqualTo(BadRequestException.class);
    }

    @Test
    public void delete_DeleteStoreById_WhenSuccessful() {
            Assertions.assertThatCode(() -> this.storeController.delete(StoreUtil.savedStore().getId()))
                            .doesNotThrowAnyException();
            ResponseEntity<Void> response = this.storeController.delete(StoreUtil.savedStore().getId());
            Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    }
}