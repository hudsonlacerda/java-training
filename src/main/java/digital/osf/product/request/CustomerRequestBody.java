package digital.osf.product.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import digital.osf.product.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestBody {
    @Size(max = 255)
    @NotEmpty
    private String firstName;

    @Size(max = 255)
    @NotEmpty
    private String lastName;

    @Size(max = 25)
    private String phone;

    @Size(max = 255)
    @NotEmpty
    private String email;

    @Size(max = 255)
    private String street;

    @Size(max = 50)
    private String city;

    @Size(max = 25)
    private String state;

    @Size(max = 5)
    private String zipCode;

    public Customer convert() {
        return Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .email(email)
                .street(street)
                .city(city)
                .state(state)
                .zipCode(zipCode).build();
    }

}
