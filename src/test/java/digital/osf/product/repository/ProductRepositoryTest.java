package digital.osf.product.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import digital.osf.product.model.Product;
import digital.osf.product.util.ProductUtil;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        this.productRepository.saveAll(ProductUtil.listProductToPersist());
    }

    @Test
    void findByCategoryCategoryName_ReturnProductFromSpecificCategory_WhenSuccessful() {
        List<Product> products = this.productRepository.findByCategoryCategoryName("GUN");
        Assertions.assertThat(products).isNotNull();
    }

}
