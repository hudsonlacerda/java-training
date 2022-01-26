package digital.osf.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import digital.osf.product.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

}