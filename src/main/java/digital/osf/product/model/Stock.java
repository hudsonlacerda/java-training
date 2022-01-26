package digital.osf.product.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stocks", schema = "osf")
public class Stock {

    @EmbeddedId
    private StockId id;

    @Column(name = "quantity")
    private Integer quantity;

}
