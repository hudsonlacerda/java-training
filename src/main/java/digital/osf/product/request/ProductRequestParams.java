package digital.osf.product.request;

import java.util.ArrayList;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.util.StringUtils;

import digital.osf.product.model.Category;
import digital.osf.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestParams {

    private String category;

    public Specification<Product> toSpec() {
        return (root, query, builder) -> {
            ArrayList<Predicate> predicates = new ArrayList<Predicate>();

            Join<Product, Category> categoryJoin = root.join("category");

            if (StringUtils.hasText(this.category))
                predicates.add(builder.equal(categoryJoin.<String>get("categoryName"), this.category));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
