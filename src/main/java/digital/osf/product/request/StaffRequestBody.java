package digital.osf.product.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import digital.osf.product.model.Staff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffRequestBody {
    @Size(max = 50)
    @NotEmpty
    private String firstName;

    @Size(max = 50)
    @NotEmpty
    private String lastName;

    @Size(max = 255)
    @NotEmpty
    private String email;

    @Size(max = 25)
    private String phone;

    @NotNull
    private Boolean active;

    @NotNull
    private Integer store;

    @NotNull
    private Integer manager;

    public Staff convert() {
        return Staff.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .active(active)
                .build();
    }
}
