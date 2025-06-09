package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.DepartmentCreateRequestDTO;
import com.example.employeemanagementsystem.dto.DepartmentResponseDTO;
import com.example.employeemanagementsystem.dto.DepartmentUpdateRequestDTO;
import com.example.employeemanagementsystem.dto.PagedResponse;
import com.example.employeemanagementsystem.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentCreateRequestDTO requestDTO) {
        DepartmentResponseDTO createdDepartment = departmentService.createDepartment(requestDTO);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<DepartmentResponseDTO>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "false") boolean expand
    ) {
        PagedResponse<DepartmentResponseDTO> departments = departmentService.getAllDepartments(page, size, expand);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean expand
    ) {
        DepartmentResponseDTO department = departmentService.getDepartmentById(id, expand);
        return ResponseEntity.ok(department);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentUpdateRequestDTO requestDTO) {
        DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}