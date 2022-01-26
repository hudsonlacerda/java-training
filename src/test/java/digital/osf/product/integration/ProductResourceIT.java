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

import digital.osf.product.model.Brand;
import digital.osf.product.model.Category;
import digital.osf.product.model.Product;
import digital.osf.product.repository.BrandRepository;
import digital.osf.product.repository.CategoryRepository;
import digital.osf.product.repository.ProductRepository;
import digital.osf.product.request.ProductRequestBody;
import digital.osf.product.util.ProductUtil;
import digital.osf.product.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ProductResourceIT {

    @Autowired
    @Qualifier("restTemplate")
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

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
    public void findByCategory_ReturnProductFromSpecificCategory_WhenSuccessful() {
        this.productRepository.saveAll(ProductUtil.listProductToPersist());

        ResponseEntity<PageableResponse<Product>> response = restTemplate.exchange("/products?category=GUN", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Product>>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<Product> products = response.getBody();

        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products).allSatisfy(p -> p.getCategory().getCategoryName().equals("GUN"));
    }

    @Test
    public void findAll_ReturnPagebleProducts_WhenSuccessful() {
        this.productRepository.saveAll(ProductUtil.listProductToPersist());

        ResponseEntity<PageableResponse<Product>> response = this.restTemplate.exchange("/products", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Product>>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Page<Product> products = response.getBody();

        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products).isNotEmpty();
        Assertions.assertThat(products.getNumberOfElements()).isEqualTo(ProductUtil.listProductToPersist().size());
    }

    @Test
    public void findById_ReturnProductFromSpecificId_WhenSuccessful() {
        Product savedProduct = this.productRepository.save(ProductUtil.productToPersist());

        ResponseEntity<Product> response = this.restTemplate.getForEntity("/products/{id}", Product.class,
                savedProduct.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product product = response.getBody();

        Assertions.assertThat(product).isNotNull();
        Assertions.assertThat(product).isEqualTo(savedProduct);
    }

    @Test
    public void create_PersistAndReturnNewProduct_WhenSuccessful() {
        Category category = this.categoryRepository.save(ProductUtil.categoryToPersist());
        Brand brand = this.brandRepository.save(ProductUtil.brandToPersist());

        ProductRequestBody requestBody = ProductUtil.productRequestBody();
        requestBody.setCategory(category.getId());
        requestBody.setBrand(brand.getId());

        ResponseEntity<Product> response = restTemplate.exchange("/products", HttpMethod.POST,
                new HttpEntity<ProductRequestBody>(requestBody),
                new ParameterizedTypeReference<Product>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Product createdProduct = response.getBody();

        Assertions.assertThat(createdProduct).isNotNull();
        Assertions.assertThat(createdProduct.getId()).isNotNull();
        Assertions.assertThat(createdProduct.getCategory()).isEqualTo(category);
        Assertions.assertThat(createdProduct.getBrand()).isEqualTo(brand);
    }

    @Test
    public void create_BadRequest_WhenCategoryOrBrandNotExists() {
        ProductRequestBody requestBody = ProductUtil.productRequestBody();
        requestBody.setCategory(1);
        requestBody.setBrand(1);

        ResponseEntity<Product> response = restTemplate.exchange("/products", HttpMethod.POST,
                new HttpEntity<ProductRequestBody>(requestBody),
                new ParameterizedTypeReference<Product>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void update_UpdateAndReturnExistingProduct_WhenSuccessful() {
        Category category = this.categoryRepository.save(ProductUtil.categoryToPersist());
        Brand brand = this.brandRepository.save(ProductUtil.brandToPersist());

        ProductRequestBody requestBody = ProductUtil.productRequestBody();
        requestBody.setCategory(category.getId());
        requestBody.setBrand(brand.getId());

        ResponseEntity<Product> postResponse = restTemplate.exchange("/products", HttpMethod.POST,
                new HttpEntity<ProductRequestBody>(requestBody),
                new ParameterizedTypeReference<Product>() {
                });

        requestBody.setProductName("musket");
        requestBody.setModelYear(1820);

        ResponseEntity<Product> putResponse = restTemplate.exchange("/products/" + postResponse.getBody().getId(),
                HttpMethod.PUT,
                new HttpEntity<ProductRequestBody>(requestBody),
                new ParameterizedTypeReference<Product>() {
                });

        Assertions.assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(postResponse.getBody()).isNotEqualTo(putResponse.getBody());
    }

    @Test
    public void delete_DeleteProductById_WhenSuccessful() {
        Product savedProduct = this.productRepository.save(ProductUtil.productToPersist());

        ResponseEntity<Void> response = this.restTemplate.exchange("/products/{id}", HttpMethod.DELETE, null,
                Void.class, savedProduct.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<Product> pOptional = this.productRepository.findById(savedProduct.getId());

        Assertions.assertThat(pOptional.isEmpty()).isTrue();
    }
}
