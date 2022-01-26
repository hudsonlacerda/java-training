package digital.osf.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import digital.osf.product.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}