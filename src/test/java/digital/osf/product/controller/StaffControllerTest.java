package digital.osf.product.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import digital.osf.product.exception.BadRequestException;
import digital.osf.product.model.Staff;
import digital.osf.product.request.StaffRequestBody;
import digital.osf.product.request.StaffRequestParams;
import digital.osf.product.service.StaffService;
import digital.osf.product.util.StaffUtil;


@ExtendWith(SpringExtension.class)
public class StaffControllerTest {

    @InjectMocks
    private StaffController staffController;

    @Mock
    private StaffService staffService;

    @BeforeEach
    void setup() {
            PageImpl<Staff> staffs = new PageImpl<>(StaffUtil.savedStaffs());
            Mockito.when(this.staffService.findById(StaffUtil.savedStaff().getId())).thenReturn(StaffUtil.savedStaff());
            Mockito.when(this.staffService.findAll(ArgumentMatchers.any(), ArgumentMatchers.any(Pageable.class))).thenReturn(staffs);
            Mockito.when(this.staffService.create(ArgumentMatchers.any(StaffRequestBody.class))).thenReturn(StaffUtil.savedStaff());
            Mockito.when(this.staffService.update(ArgumentMatchers.eq(StaffUtil.savedStaff().getId()), ArgumentMatchers.any(StaffRequestBody.class))).thenReturn(StaffUtil.savedStaff());
            Mockito.when(this.staffService.update( AdditionalMatchers.not(ArgumentMatchers.eq(StaffUtil.savedStaff().getId())), ArgumentMatchers.any(StaffRequestBody.class))).thenThrow(new BadRequestException("staff id not found"));
            Mockito.doNothing().when(this.staffService).delete(ArgumentMatchers.anyInt());
    }

    @Test
    public void findByAll_ReturnstaffFromSpecificFirstName_WhenSuccessful() {
            ResponseEntity<Page<Staff>> response = this.staffController.findAll(StaffRequestParams.builder().firstName("Hudson").build(), PageRequest.of(0, 1));

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<Staff> staffs = response.getBody().getContent();

            Assertions.assertThat(staffs).isNotNull();
            Assertions.assertThat(staffs).isNotEmpty();
            Assertions.assertThat(staffs.size()).isEqualTo(StaffUtil.savedStaffs().size());
    }

    @Test
    public void findAll_ReturnPageblestaffs_WhenSuccessful() {
            ResponseEntity<Page<Staff>> response = this.staffController.findAll(new StaffRequestParams(), PageRequest.of(1, 1));

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Page<Staff> staffs = response.getBody();

            Assertions.assertThat(staffs).isNotNull();
            Assertions.assertThat(staffs).isNotEmpty();
            Assertions.assertThat(staffs.getNumberOfElements()).isEqualTo(StaffUtil.savedStaffs().size());
    }

    @Test
    public void findById_ReturnstaffFromSpecificId_WhenSuccessful() {
            ResponseEntity<Staff> response = this.staffController.findById(StaffUtil.savedStaff().getId());

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Staff staff = response.getBody();

            Assertions.assertThat(staff).isNotNull();
    }

    @Test
    public void create_PersistAndReturnNewstaff_WhenSuccessful() {
            StaffRequestBody requestBody = StaffUtil.staffRequestBody();
            ResponseEntity<Staff> response = this.staffController.create(requestBody);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            Staff createdstaff = response.getBody();

            Assertions.assertThat(createdstaff).isNotNull();
            Assertions.assertThat(createdstaff.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingstaff_WhenSuccessful() {
            StaffRequestBody requestBody = StaffUtil.staffRequestBody();
            ResponseEntity<Staff> response = this.staffController.update(StaffUtil.savedStaff().getId(),
                            requestBody);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            Staff createdstaff = response.getBody();

            Assertions.assertThat(createdstaff).isNotNull();
            Assertions.assertThat(createdstaff.getId()).isNotNull();
    }

    @Test
    public void update_ReturnBadRequest_WhenNotexists() {
            StaffRequestBody requestBody = StaffUtil.staffRequestBody();
            Exception badRequest = assertThrows(Exception.class, () -> this.staffController
                            .update(StaffUtil.savedStaff().getId() + 1, requestBody));
            Assertions.assertThat(badRequest.getClass()).isEqualTo(BadRequestException.class);
    }

    @Test
    public void delete_DeletestaffById_WhenSuccessful() {
            Assertions.assertThatCode(() -> this.staffController.delete(StaffUtil.savedStaff().getId()))
                            .doesNotThrowAnyException();
            ResponseEntity<Void> response = this.staffController.delete(StaffUtil.savedStaff().getId());
            Assertions.assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
    }
}
