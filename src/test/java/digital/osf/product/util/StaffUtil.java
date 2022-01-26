package digital.osf.product.util;

import java.util.ArrayList;
import java.util.List;

import digital.osf.product.model.Staff;
import digital.osf.product.model.Store;
import digital.osf.product.request.StaffRequestBody;

public class StaffUtil {

    public static StaffRequestBody staffRequestBody() {
        return StaffRequestBody.builder()
                .firstName("Hudson")
                .lastName("Lacerda")
                .email("hudson@ldc.com")
                .phone("8888-8888")
                .active(true)
                .store(1)
                .manager(1)
                .build();
    }

    public static Staff staffToPersist() {
        Store store = StoreUtil.storeToPersist();
        return Staff.builder()
                .firstName("Hudson")
                .lastName("Lacerda")
                .email("hudson@ldc.com")
                .phone("8888-8888")
                .active(true)
                .manager(managerToPersist(store))
                .store(store)
                .build();
    }

    public static Staff savedStaff() {
        Store store = StoreUtil.storeToPersist();
        return Staff.builder().id(1)
                .firstName("Hudson")
                .lastName("Lacerda")
                .email("hudson@ldc.com")
                .phone("8888-8888")
                .active(true)
                .manager(managerToPersist(store))
                .store(store)
                .staffs(new ArrayList<>())
                .build();
    }

    public static List<Staff> listStaffToPersist() {
        return List.of(staffToPersist());
    }

    public static List<Staff> savedStaffs() {
        return List.of(savedStaff());
    }

    public static Staff managerToPersist(Store store) {
        return Staff.builder().firstName("Morpheus").lastName("Silva").email("morp.sil@ldc.com").store(store).phone("8888-8888").active(true).build();
    }
}
