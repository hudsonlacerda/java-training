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

import digital.osf.product.model.Store;
import digital.osf.product.repository.StoreRepository;
import digital.osf.product.request.StoreRequestBody;
import digital.osf.product.util.StoreUtil;
import digital.osf.product.wrapper.PageableResponse;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class StoreResourceIT {

    @Autowired
    @Qualifier("restTemplate")
    private TestRestTemplate restTemplate;

    @Autowired
    private StoreRepository storeRepository;

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
    public void findByCategory_ReturnStoreFromSpecificCategory_WhenSuccessful() {
        this.storeRepository.saveAll(StoreUtil.listStoreToPersist());

        ResponseEntity<PageableResponse<Store>> response = restTemplate.exchange("/stores?city=city1", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Store>>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<Store> stores = response.getBody();

        Assertions.assertThat(stores).isNotNull();
        Assertions.assertThat(stores).allSatisfy(s -> s.getCity().equals("city1"));
    }

    @Test
    public void findAll_ReturnPagebleStores_WhenSuccessful() {
        this.storeRepository.saveAll(StoreUtil.listStoreToPersist());

        ResponseEntity<PageableResponse<Store>> response = this.restTemplate.exchange("/stores", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Store>>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Page<Store> stores = response.getBody();

        Assertions.assertThat(stores).isNotNull();
        Assertions.assertThat(stores).isNotEmpty();
        Assertions.assertThat(stores.getNumberOfElements()).isEqualTo(StoreUtil.savedStores().size());
    }

    @Test
    public void findById_ReturnStoreFromSpecificId_WhenSuccessful() {
        Store savedStore = this.storeRepository.save(StoreUtil.storeToPersist());

        ResponseEntity<Store> response = this.restTemplate.getForEntity("/stores/{id}", Store.class,
                savedStore.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Store store = response.getBody();

        Assertions.assertThat(store).isNotNull();
        Assertions.assertThat(store).isEqualTo(savedStore);
    }

    @Test
    public void create_PersistAndReturnNewStore_WhenSuccessful() {
        StoreRequestBody requestBody = StoreUtil.storeRequestBody();

        ResponseEntity<Store> response = restTemplate.exchange("/stores", HttpMethod.POST,
                new HttpEntity<StoreRequestBody>(requestBody),
                new ParameterizedTypeReference<Store>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Store createdStore = response.getBody();

        Assertions.assertThat(createdStore).isNotNull();
        Assertions.assertThat(createdStore.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingStore_WhenSuccessful() {
        StoreRequestBody requestBody = StoreUtil.storeRequestBody();

        ResponseEntity<Store> postResponse = restTemplate.exchange("/stores", HttpMethod.POST,
                new HttpEntity<StoreRequestBody>(requestBody),
                new ParameterizedTypeReference<Store>() {
                });

        requestBody.setPhone("8888-8888");
        requestBody.setEmail("store@osf.digital");

        ResponseEntity<Store> putResponse = restTemplate.exchange("/stores/" + postResponse.getBody().getId(),
                HttpMethod.PUT,
                new HttpEntity<StoreRequestBody>(requestBody),
                new ParameterizedTypeReference<Store>() {
                });

        Assertions.assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(postResponse.getBody()).isNotEqualTo(putResponse.getBody());
    }

    @Test
    public void delete_DeleteStoreById_WhenSuccessful() {
        Store savedstore = this.storeRepository.save(StoreUtil.storeToPersist());

        ResponseEntity<Void> response = this.restTemplate.exchange("/stores/{id}", HttpMethod.DELETE, null,
                Void.class, savedstore.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<Store> pOptional = this.storeRepository.findById(savedstore.getId());

        Assertions.assertThat(pOptional.isEmpty()).isTrue();
    }
}
