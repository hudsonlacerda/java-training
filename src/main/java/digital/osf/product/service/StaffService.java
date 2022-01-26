package digital.osf.product.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import digital.osf.product.exception.BadRequestException;
import digital.osf.product.model.Staff;
import digital.osf.product.model.Store;
import digital.osf.product.repository.StaffRepository;
import digital.osf.product.request.StaffRequestBody;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final StoreService storeService;

    public Page<Staff> findAll(Specification<Staff> spec, Pageable pageable) {
        return this.staffRepository.findAll(spec, pageable);
    }

    public Staff findById(Integer id) {
        return this.staffRepository.findById(id).orElseThrow(() -> new BadRequestException("staff not found"));
    }

    public Staff create(@Valid StaffRequestBody requestBody) {
        Staff staff = this.resolve(requestBody);

        staff = this.staffRepository.save(staff);

        return staff;
    }

    public Staff update(Integer id, @Valid StaffRequestBody requestBody) {
        this.findById(id);

        Staff staff = this.resolve(requestBody);
        staff.setId(id);

        this.staffRepository.save(staff);

        return staff;
    }

    public void delete(Integer id) {
        Staff staff = this.findById(id);

        if (!staff.getStaffs().isEmpty())
            throw new BadRequestException("This manager is responsible for some employees. Please unlink before performing the delete operation.");

        this.staffRepository.delete(staff);
    }

    private Staff resolve(StaffRequestBody requestBody) {
        Staff staff = requestBody.convert();

        Store store = this.storeService.findById(requestBody.getStore());

        Staff manager = this.findById(requestBody.getManager());

        staff.setStore(store);
        staff.setManager(manager);
        return staff;
    }
}
