package digital.osf.product.integration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import digital.osf.product.model.Staff;
import digital.osf.product.model.Store;
import digital.osf.product.repository.StaffRepository;
import digital.osf.product.request.StaffRequestBody;
import digital.osf.product.util.StaffUtil;
import digital.osf.product.util.StoreUtil;
import digital.osf.product.wrapper.PageableResponse;
import net.minidev.json.JSONObject;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class StaffResourceIT {

    @Autowired
    @Qualifier("restTemplate")
    private TestRestTemplate restTemplate;

    @Autowired
    private StaffRepository staffRepository;

    @Lazy
    @TestConfiguration
    static class Config {
        @Bean(name = "restTemplate")
        public TestRestTemplate restTemplateCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port);
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    public void findAll_ReturnStaffFromSpecificFirstName_WhenSuccessful() {
        this.staffRepository.saveAll(StaffUtil.listStaffToPersist());

        ResponseEntity<PageableResponse<JSONObject>> response = restTemplate.exchange("/staffs?firstName=Hudson",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<JSONObject>>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Page<JSONObject> staffs = response.getBody();

        Assertions.assertThat(staffs).isNotNull();
        Assertions.assertThat(staffs).allSatisfy(s -> s.getAsString("firstName").equals("Hudson"));
    }

    @Test
    public void findAll_ReturnPagebleStaffs_WhenSuccessful() {
        this.staffRepository.saveAll(StaffUtil.listStaffToPersist());

        ResponseEntity<PageableResponse<JSONObject>> response = this.restTemplate.exchange("/staffs", HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<JSONObject>>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Page<JSONObject> staffs = response.getBody();

        Assertions.assertThat(staffs).isNotNull();
        Assertions.assertThat(staffs).isNotEmpty();
        Assertions.assertThat(staffs.getNumberOfElements()).isEqualTo(StaffUtil.listStaffToPersist().size() + 1);
    }

    @Test
    public void findById_ReturnStaffFromSpecificId_WhenSuccessful() {
        Staff savedstaff = this.staffRepository.save(StaffUtil.staffToPersist());

        ResponseEntity<JSONObject> response = this.restTemplate.getForEntity("/staffs/{id}", JSONObject.class,
                savedstaff.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONObject staff = response.getBody();

        Assertions.assertThat(staff).isNotNull();
        Assertions.assertThat(staff.getAsNumber("id")).isEqualTo(savedstaff.getId());
    }

    @Test
    public void create_PersistAndReturnNewstaff_WhenSuccessful() {
        Store store = StoreUtil.storeToPersist();
        Staff manager = StaffUtil.managerToPersist(store);
        manager = this.staffRepository.save(manager);

        StaffRequestBody requestBody = StaffUtil.staffRequestBody();
        requestBody.setStore(manager.getStore().getId());
        requestBody.setManager(manager.getId());

        ResponseEntity<JSONObject> response = restTemplate.exchange("/staffs", HttpMethod.POST,
                new HttpEntity<StaffRequestBody>(requestBody),
                new ParameterizedTypeReference<JSONObject>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        JSONObject createdStaff = response.getBody();

        Assertions.assertThat(createdStaff).isNotNull();
        Assertions.assertThat(createdStaff.getAsNumber("id")).isNotNull();
    }

    @Test
    public void create_BadRequest_WhenCategoryOrBrandNotExists() {
        StaffRequestBody requestBody = StaffUtil.staffRequestBody();
        requestBody.setStore(1);
        requestBody.setManager(1);

        ResponseEntity<JSONObject> response = restTemplate.exchange("/staffs", HttpMethod.POST,
                new HttpEntity<StaffRequestBody>(requestBody),
                new ParameterizedTypeReference<JSONObject>() {
                });

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void update_UpdateAndReturnExistingstaff_WhenSuccessful() {
        Store store = StoreUtil.storeToPersist();
        Staff manager = StaffUtil.managerToPersist(store);
        manager = this.staffRepository.save(manager);

        StaffRequestBody requestBody = StaffUtil.staffRequestBody();
        requestBody.setStore(manager.getStore().getId());
        requestBody.setManager(manager.getId());

        ResponseEntity<JSONObject> postResponse = restTemplate.exchange("/staffs", HttpMethod.POST,
                new HttpEntity<StaffRequestBody>(requestBody),
                new ParameterizedTypeReference<JSONObject>() {
                });

        requestBody.setPhone("(85)9999-9999)");

        ResponseEntity<JSONObject> putResponse = restTemplate.exchange("/staffs/" + postResponse.getBody().getAsNumber("id"),
                HttpMethod.PUT,
                new HttpEntity<StaffRequestBody>(requestBody),
                new ParameterizedTypeReference<JSONObject>() {
                });

        Assertions.assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(postResponse.getBody()).isNotEqualTo(putResponse.getBody());
    }

    @Test
    public void delete_DeletestaffById_WhenSuccessful() {
        Staff savedStaff = this.staffRepository.save(StaffUtil.staffToPersist());

        ResponseEntity<Void> response = this.restTemplate.exchange("/staffs/{id}", HttpMethod.DELETE, null,
                Void.class, savedStaff.getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<Staff> pOptional = this.staffRepository.findById(savedStaff.getId());

        Assertions.assertThat(pOptional.isEmpty()).isTrue();
    }
}
