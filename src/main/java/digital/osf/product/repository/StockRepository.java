package digital.osf.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import digital.osf.product.model.Stock;
import digital.osf.product.model.StockId;

public interface StockRepository extends JpaRepository<Stock, StockId> {

}