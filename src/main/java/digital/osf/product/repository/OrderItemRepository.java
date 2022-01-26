package digital.osf.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import digital.osf.product.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}