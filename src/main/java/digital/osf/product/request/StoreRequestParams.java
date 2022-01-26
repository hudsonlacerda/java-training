package digital.osf.product.request;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import digital.osf.product.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestParams {
    private String storeName;
    private String phone;
    private String email;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public Specification<Store> toSpec() {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(storeName))
                predicates.add(builder.equal(root.<String>get("storeName"), storeName));
            if (StringUtils.hasText(phone))
                predicates.add(builder.equal(root.<String>get("phone"), phone));
            if (StringUtils.hasText(email))
                predicates.add(builder.equal(root.<String>get("email"), email));
            if (StringUtils.hasText(street))
                predicates.add(builder.equal(root.<String>get("street"), street));
            if (StringUtils.hasText(city))
                predicates.add(builder.equal(root.<String>get("city"), city));
            if (StringUtils.hasText(state))
                predicates.add(builder.equal(root.<String>get("state"), state));
            if (StringUtils.hasText(zipCode))
                predicates.add(builder.equal(root.<String>get("zipCode"), zipCode));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
