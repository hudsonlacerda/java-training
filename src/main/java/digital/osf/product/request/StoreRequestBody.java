package digital.osf.product.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import digital.osf.product.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestBody {
    @NotEmpty
    @Size(max = 255)
    private String storeName;
    @NotEmpty
    @Size(max = 25)
    private String phone;
    @NotEmpty
    @Size(max = 255)
    private String email;
    @NotEmpty
    @Size(max = 255)
    private String street;
    @NotEmpty
    @Size(max = 255)
    private String city;
    @NotEmpty
    @Size(max = 10)
    private String state;
    @NotEmpty
    @Size(max = 5)
    private String zipCode;

    public Store convert() {
        return Store.builder()
                .storeName(storeName)
                .phone(phone)
                .email(email)
                .street(street)
                .city(city)
                .state(state)
                .zipCode(zipCode).build();
    }
}
