package digital.osf.product.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import digital.osf.product.exception.BadRequestException;
import digital.osf.product.model.Brand;
import digital.osf.product.model.Category;
import digital.osf.product.model.Product;
import digital.osf.product.repository.BrandRepository;
import digital.osf.product.repository.CategoryRepository;
import digital.osf.product.repository.ProductRepository;
import digital.osf.product.request.ProductRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public List<Product> findByCategory(String category) {
        return productRepository.findByCategoryCategoryName(category);
    }

    public Page<Product> findAll(Specification<Product> specification, Pageable pageable) {
        return this.productRepository.findAll(specification, pageable);
    }

    public Product findById(Integer id) {
        return this.productRepository.findById(id).orElseThrow(() -> new BadRequestException("product id not found"));

    }

    public Product create(ProductRequestBody requestBody) {
        Product product = this.resolve(requestBody);
        return this.productRepository.save(product);
    }

    public Product update(Integer id, @Valid ProductRequestBody requestBody) {
        this.findById(id);

        Product newProduct = this.resolve(requestBody);
        newProduct.setId(id);

        this.productRepository.save(newProduct);

        return newProduct;
    }

    public void delete(Integer id) {
        this.productRepository.delete(this.findById(id));
    }

    private Product resolve(ProductRequestBody requestBody) {
        Product product = requestBody.converter();

        Optional<Category> categoryOptional = this.categoryRepository.findById(requestBody.getCategory());

        if (categoryOptional.isEmpty())
            throw new BadRequestException("category not exists");

        Optional<Brand> brandOptional = this.brandRepository.findById(requestBody.getBrand());

        if (brandOptional.isEmpty())
            throw new BadRequestException("brand not exists");

        product.setBrand(brandOptional.get());
        product.setCategory(categoryOptional.get());
        return product;
    }

}
