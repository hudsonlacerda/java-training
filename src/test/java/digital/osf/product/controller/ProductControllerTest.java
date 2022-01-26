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
import digital.osf.product.model.Product;
import digital.osf.product.request.ProductRequestBody;
import digital.osf.product.request.ProductRequestParams;
import digital.osf.product.service.ProductService;
import digital.osf.product.util.ProductUtil;

@ExtendWith(SpringExtension.class)
public class ProductControllerTest {

        @InjectMocks
        private ProductController productController;

        @Mock
        private ProductService productService;

        @BeforeEach
        void setup() {
                PageImpl<Product> products = new PageImpl<>(ProductUtil.savedProducts());
                Mockito.when(this.productService.findByCategory(ArgumentMatchers.anyString()))
                                .thenReturn(List.of(ProductUtil.savedProduct()));
                Mockito.when(this.productService.findById(ProductUtil.savedProduct().getId()))
                                .thenReturn(ProductUtil.savedProduct());
                Mockito.when(this.productService.findAll(ArgumentMatchers.any(), ArgumentMatchers.any(Pageable.class)))
                                .thenReturn(products);
                Mockito.when(this.productService.create(ArgumentMatchers.any(ProductRequestBody.class)))
                                .thenReturn(ProductUtil.savedProduct());
                Mockito.when(this.productService.update(ArgumentMatchers.eq(ProductUtil.savedProduct().getId()), ArgumentMatchers.any(ProductRequestBody.class)))
                                .thenReturn(ProductUtil.savedProduct());
                Mockito.when(this.productService.update( AdditionalMatchers.not(ArgumentMatchers.eq(ProductUtil.savedProduct().getId())), ArgumentMatchers.any(ProductRequestBody.class)))
                                .thenThrow(new BadRequestException("product id not found"));
                Mockito.doNothing().when(this.productService).delete(ArgumentMatchers.anyInt());
        }

        @Test
        public void findByCategory_ReturnProductFromSpecificCategory_WhenSuccessful() {
                ResponseEntity<Page<Product>> response = this.productController.findAll(ProductRequestParams.builder().category("").build(), PageRequest.of(0, 1));

                Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                List<Product> products = response.getBody().getContent();

                Assertions.assertThat(products).isNotNull();
                Assertions.assertThat(products).isNotEmpty();
                Assertions.assertThat(products.size()).isEqualTo(ProductUtil.savedProducts().size());
        }

        @Test
        public void findAll_ReturnPagebleProducts_WhenSuccessful() {
                ResponseEntity<Page<Product>> response = this.productController.findAll(new ProductRequestParams(), PageRequest.of(1, 1));

                Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Page<Product> products = response.getBody();

                Assertions.assertThat(products).isNotNull();
                Assertions.assertThat(products).isNotEmpty();
                Assertions.assertThat(products.getNumberOfElements()).isEqualTo(ProductUtil.savedProducts().size());
        }

        @Test
        public void findById_ReturnProductFromSpecificId_WhenSuccessful() {
                ResponseEntity<Product> response = this.productController.findById(ProductUtil.savedProduct().getId());

                Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                Product product = response.getBody();

                Assertions.assertThat(product).isNotNull();
                Assertions.assertThat(product).isEqualTo(ProductUtil.savedProduct());
        }

        @Test
        public void create_PersistAndReturnNewProduct_WhenSuccessful() {
                ProductRequestBody requestBody = ProductUtil.productRequestBody();
                ResponseEntity<Product> response = this.productController.create(requestBody);

                Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

                Product createdProduct = response.getBody();

                Assertions.assertThat(createdProduct).isNotNull();
                Assertions.assertThat(createdProduct.getId()).isNotNull();
        }

        @Test
        public void update_UpdateAndReturnExistingProduct_WhenSuccessful() {
                ProductRequestBody requestBody = ProductUtil.productRequestBody();
                ResponseEntity<Product> response = this.productController.update(ProductUtil.savedProduct().getId(),
                                requestBody);

                Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

                Product createdProduct = response.getBody();

                Assertions.assertThat(createdProduct).isNotNull();
                Assertions.assertThat(createdProduct.getId()).isNotNull();
        }

        @Test
        public void update_ReturnBadRequest_WhenNotexists() {
                ProductRequestBody requestBody = ProductUtil.productRequestBody();
                Exception badRequest = assertThrows(Exception.class, () -> this.productController
                                .update(ProductUtil.savedProduct().getId() + 1, requestBody));
                Assertions.assertThat(badRequest.getClass()).isEqualTo(BadRequestException.class);
        }

        @Test
        public void delete_DeleteProductById_WhenSuccessful() {
                Assertions.assertThatCode(() -> this.productController.delete(ProductUtil.savedProduct().getId()))
                                .doesNotThrowAnyException();
                ResponseEntity<Void> response = this.productController.delete(ProductUtil.savedProduct().getId());
                Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
        }
}
