package digital.osf.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import digital.osf.product.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}