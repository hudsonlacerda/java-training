package digital.osf.product.request;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import digital.osf.product.model.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffRequestParams {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean active;

    public Specification<Staff> toSpec() {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(firstName))
                predicates.add(builder.equal(root.<String>get("firstName"), this.firstName));

            if (StringUtils.hasText(lastName))
                predicates.add(builder.equal(root.<String>get("lastName"), this.lastName));

            if (StringUtils.hasText(email))
                predicates.add(builder.equal(root.<String>get("email"), this.email));

            if (StringUtils.hasText(phone))
                predicates.add(builder.equal(root.<String>get("phone"), this.phone));

            if (active != null)
                predicates.add(builder.equal(root.<Boolean>get("active"), this.active));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
