package digital.osf.product.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import digital.osf.product.model.Staff;
import digital.osf.product.request.StaffRequestBody;
import digital.osf.product.request.StaffRequestParams;
import digital.osf.product.service.StaffService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public ResponseEntity<Page<Staff>> findAll(StaffRequestParams params, Pageable pageable) {
        Page<Staff> staffs = this.staffService.findAll(params.toSpec(), pageable);
        return ResponseEntity.ok().body(staffs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> findById(@PathVariable("id") Integer id) {
        Staff staff = this.staffService.findById(id);
        return ResponseEntity.ok().body(staff);
    }

    @PostMapping
    public ResponseEntity<Staff> create(@RequestBody @Valid StaffRequestBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.staffService.create(requestBody));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Staff> update(@PathVariable("id") Integer id,
            @RequestBody @Valid StaffRequestBody requestBody) {
        return ResponseEntity.ok().body(this.staffService.update(id, requestBody));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        this.staffService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

