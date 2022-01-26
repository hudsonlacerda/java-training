package digital.osf.product.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import digital.osf.product.model.Staff;
import digital.osf.product.repository.StaffRepository;
import digital.osf.product.request.StaffRequestBody;
import digital.osf.product.request.StaffRequestParams;
import digital.osf.product.util.StaffUtil;
import digital.osf.product.util.StoreUtil;

@ExtendWith(SpringExtension.class)
public class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StaffService staffService;

    @BeforeEach
    void setup() {
        PageImpl<Staff> staffs = new PageImpl<>(StaffUtil.savedStaffs());
        Mockito.when(this.staffRepository.save(ArgumentMatchers.any(Staff.class))).thenReturn(StaffUtil.savedStaff());
        Mockito.doNothing().when(this.staffRepository).delete(ArgumentMatchers.any(Staff.class));
        Mockito.when(this.staffRepository.findById(StaffUtil.savedStaff().getId())).thenReturn(Optional.of(StaffUtil.savedStaff()));
        Mockito.when(this.staffRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.any(Pageable.class))).thenReturn(staffs);
        Mockito.when(this.storeService.findById(ArgumentMatchers.anyInt())).thenReturn(StoreUtil.savedStore());
    }

    @Test
    public void findAll_ReturnStaffFromSpecificFirstName_WhenSuccessful() {
        Page<Staff> staffs = this.staffService.findAll(StaffRequestParams.builder().firstName("Hudson").build().toSpec(), PageRequest.of(1, 1));
        Assertions.assertThat(staffs).isNotNull();
        Assertions.assertThat(staffs).isNotEmpty();
        Assertions.assertThat(staffs.getNumberOfElements()).isEqualTo(StaffUtil.savedStaffs().size());
    }

    @Test
    public void findAll_ReturnPagebleStaffs_WhenSuccessful() {
        Page<Staff> staffs = this.staffService.findAll(new StaffRequestParams().toSpec(), PageRequest.of(1, 1));

        Assertions.assertThat(staffs).isNotNull();
        Assertions.assertThat(staffs).isNotEmpty();
        Assertions.assertThat(staffs.getNumberOfElements()).isEqualTo(StaffUtil.savedStaffs().size());
    }

    @Test
    public void findById_ReturnStaffFromSpecificId_WhenSuccessful() {
        Staff staff = this.staffService.findById(StaffUtil.savedStaff().getId());

        Assertions.assertThat(staff).isNotNull();
    }

    @Test
    public void create_PersistAndReturnNewStaff_WhenSuccessful() {
        StaffRequestBody requestBody = StaffUtil.staffRequestBody();
        Staff createdStaff = this.staffService.create(requestBody);

        Assertions.assertThat(createdStaff).isNotNull();
        Assertions.assertThat(createdStaff.getId()).isNotNull();
    }

    @Test
    public void update_UpdateAndReturnExistingStaff_WhenSuccessful() {
        StaffRequestBody requestBody = StaffUtil.staffRequestBody();
        Staff updatedstaff = this.staffService.update(StaffUtil.savedStaff().getId(), requestBody);

        Assertions.assertThat(updatedstaff).isNotNull();
        Assertions.assertThat(updatedstaff.getId()).isNotNull();
    }

    @Test
    public void delete_DeleteStaffById_WhenSuccessful() {
        Assertions.assertThatCode(() -> this.staffService.delete(StaffUtil.savedStaff().getId()))
                .doesNotThrowAnyException();
    }
}
