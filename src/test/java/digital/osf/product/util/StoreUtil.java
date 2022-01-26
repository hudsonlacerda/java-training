package digital.osf.product.util;

import java.util.List;

import digital.osf.product.model.Store;
import digital.osf.product.request.StoreRequestBody;

public class StoreUtil {

    public static StoreRequestBody storeRequestBody() {
        return StoreRequestBody.builder()
                .storeName("storeName")
                .phone("phone")
                .email("email")
                .street("street")
                .city("city")
                .state("state")
                .zipCode("12345")
                .build();
    }

    public static Store storeToPersist() {
        return Store.builder()
            .storeName("storeName")
            .phone("phone")
            .email("email")
            .street("street")
            .city("city")
            .state("state")
            .zipCode("12345")
            .build();
    }

    public static Store savedStore() {
        return Store.builder().id(1)
            .storeName("storeName")
            .phone("phone")
            .email("email")
            .street("street")
            .city("city")
            .state("state")
            .zipCode("12345")
            .build();
    }

    public static List<Store> listStoreToPersist() {
        return List.of(
                Store.builder().storeName("storeName1").phone("phone1").email("email1").street("street1").city("city1").state("state1").zipCode("12345").build(),
                Store.builder().storeName("storeName2").phone("phone2").email("email2").street("street2").city("city1").state("state2").zipCode("12345").build(),
                Store.builder().storeName("storeName3").phone("phone3").email("email3").street("street3").city("city4").state("state3").zipCode("12345").build(),
                Store.builder().storeName("storeName4").phone("phone4").email("email4").street("street4").city("city4").state("state4").zipCode("12345").build());
    }

    public static List<Store> savedStores() {
        return List.of(
                Store.builder().id(1).storeName("storeName1").phone("phone1").email("email1").street("street1").city("city1").state("state1").zipCode("12345").build(),
                Store.builder().id(2).storeName("storeName2").phone("phone2").email("email2").street("street2").city("city1").state("state2").zipCode("12345").build(),
                Store.builder().id(3).storeName("storeName3").phone("phone3").email("email3").street("street3").city("city4").state("state3").zipCode("12345").build(),
                Store.builder().id(4).storeName("storeName4").phone("phone4").email("email4").street("street4").city("city4").state("state4").zipCode("12345").build());
    }
}
