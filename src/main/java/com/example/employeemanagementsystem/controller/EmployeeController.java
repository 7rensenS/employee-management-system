package com.example.employeemanagementsystem.controller;

import com.example.employeemanagementsystem.dto.EmployeeCreateRequestDTO;
import com.example.employeemanagementsystem.dto.EmployeeResponseDTO;
import com.example.employeemanagementsystem.dto.EmployeeUpdateDepartmentRequestDTO;
import com.example.employeemanagementsystem.dto.EmployeeUpdateRequestDTO;
import com.example.employeemanagementsystem.dto.PagedResponse;
import com.example.employeemanagementsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeCreateRequestDTO requestDTO) {
        EmployeeResponseDTO createdEmployee = employeeService.createEmployee(requestDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<EmployeeResponseDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false, defaultValue = "false") boolean lookup
    ) {
        PagedResponse<EmployeeResponseDTO> employees = employeeService.getAllEmployees(page, size, lookup);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeResponseDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequestDTO requestDTO) {
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, requestDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PatchMapping("/{id}/department")
    public ResponseEntity<EmployeeResponseDTO> updateEmployeeDepartment(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateDepartmentRequestDTO requestDTO) {
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployeeDepartment(id, requestDTO);
        return ResponseEntity.ok(updatedEmployee);
    }
}