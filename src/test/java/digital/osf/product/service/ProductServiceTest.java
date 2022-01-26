package digital.osf.product.service;

import java.util.List;
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

import digital.osf.product.model.Product;
import digital.osf.product.repository.BrandRepository;
import digital.osf.product.repository.CategoryRepository;
import digital.osf.product.repository.ProductRepository;
import digital.osf.product.request.ProductRequestBody;
import digital.osf.product.request.ProductRequestParams;
import digital.osf.product.util.ProductUtil;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        PageImpl<Product> products = new PageImpl<>(ProductUtil.savedProducts());
        Mockito.when(this.productRepository.findByCategoryCategoryName(ArgumentMatchers.anyString())).thenReturn(List.of(ProductUtil.savedProduct()));
        Mockito.when(this.productRepository.save(ArgumentMatchers.any(Product.class))).thenReturn(ProductUtil.savedProduct());
        Mockito.doNothing().when(this.productRepository).delete(ArgumentMatchers.any(Product.class));
        Mockito.when(this.productRepository.findById(ProductUtil.savedProduct().getId())).thenReturn(Optional.of(ProductUtil.savedProduct()));
        Mockito.when(this.productRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.any(Pageable.class))).thenReturn(products);
        Mockito.when(this.categoryRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(ProductUtil.categoryToPersist()));
        Mockito.when(this.brandRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(ProductUtil.brandToPersist()));
    }

    @Test
    public void findByCategory_ReturnProductFromSpecificCategory_WhenSuccessful() {
        List<Product> products = this.productService.findByCategory("TEST_CATEGORY");
        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products).isNotEmpty();
        Assertions.assertThat(products.size()).isEqualTo(1);
        Assertions.assertThat(products.get(0)).isEqualTo(ProductUtil.savedProduct());
    }

    @Test
    public void findAll_ReturnPagebleProducts_WhenSuccessful() {
        Page<Product> products = this.productService.findAll(new ProductRequestParams().toSpec(), PageRequest.of(1, 1));

        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products).isNotEmpty();
        Assertions.assertThat(products.getNumberOfElements()).isEqualTo(ProductUtil.savedProducts().size());
    }

    @Test
    public void findById_ReturnProductFromSpecificId_WhenSuccessful() {
        Product product = this.productService.findById(ProductUtil.savedProduct().getId());

        Assertions.assertThat(product).isNotNull();
        Assertions.assertThat(product).isEqualTo(ProductUtil.savedProduct());
    }

    @Test
    public void create_PersistAndReturnNewProduct_WhenSuccessful() {
        ProductRequestBody requestBody = ProductUtil.productRequestBody();
        Product createdProduct = this.productService.create(requestBody);

        Assertions.assertThat(createdProduct).isNotNull();
        Assertions.assertThat(createdProduct.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingProduct_WhenSuccessful() {
        ProductRequestBody requestBody = ProductUtil.productRequestBody();
        Product updatedProduct = this.productService.update(ProductUtil.savedProduct().getId(), requestBody);

        Assertions.assertThat(updatedProduct).isNotNull();
        Assertions.assertThat(updatedProduct.getId()).isNotNull();
    }

    @Test
    public void delete_DeleteProductById_WhenSuccessful() {
        Assertions.assertThatCode(() -> this.productService.delete(ProductUtil.savedProduct().getId())).doesNotThrowAnyException();
    }
}
