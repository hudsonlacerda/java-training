package digital.osf.product.request;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import digital.osf.product.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestParams {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public Specification<Customer> toSpec() {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(firstName))
                predicates.add(builder.equal(root.<String>get("firstName"), this.firstName));

            if (StringUtils.hasText(lastName))
                predicates.add(builder.equal(root.<String>get("lastName"), this.lastName));

            if (StringUtils.hasText(phone))
                predicates.add(builder.equal(root.<String>get("phone"), this.phone));

            if (StringUtils.hasText(email))
                predicates.add(builder.equal(root.<String>get("email"), this.email));

            if (StringUtils.hasText(street))
                predicates.add(builder.equal(root.<String>get("street"), this.street));

            if (StringUtils.hasText(city))
                predicates.add(builder.equal(root.<String>get("city"), this.city));

            if (StringUtils.hasText(state))
                predicates.add(builder.equal(root.<String>get("state"), this.state));

            if (StringUtils.hasText(zipCode))
                predicates.add(builder.equal(root.<String>get("zipCode"), this.zipCode));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
