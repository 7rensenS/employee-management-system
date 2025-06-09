package com.example.employeemanagementsystem.service;

import com.example.employeemanagementsystem.dto.*;
import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.exception.ResourceNotFoundException;
import com.example.employeemanagementsystem.exception.ValidationException;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository; // To check for department head and employees

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentCreateRequestDTO requestDTO) {
        if (departmentRepository.existsByName(requestDTO.getName())) {
            throw new ValidationException("Department with name '" + requestDTO.getName() + "' already exists.");
        }

        Department department = new Department();
        department.setName(requestDTO.getName());
        department.setCreationDate(requestDTO.getCreationDate());

        if (requestDTO.getDepartmentHeadId() != null) {
            Employee departmentHead = employeeRepository.findById(requestDTO.getDepartmentHeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getDepartmentHeadId()));
            department.setDepartmentHead(departmentHead);
        }

        Department savedDepartment = departmentRepository.save(department);
        return mapToDepartmentResponseDTO(savedDepartment, false);
    }

    public PagedResponse<DepartmentResponseDTO> getAllDepartments(int page, int size, boolean expandEmployees) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage = departmentRepository.findAll(pageable);

        List<DepartmentResponseDTO> content = departmentPage.getContent().stream()
                .map(department -> mapToDepartmentResponseDTO(department, expandEmployees))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                departmentPage.getNumber(),
                departmentPage.getSize(),
                departmentPage.getTotalElements(),
                departmentPage.getTotalPages(),
                departmentPage.isLast(),
                departmentPage.isFirst()
        );
    }

    public DepartmentResponseDTO getDepartmentById(Long id, boolean expandEmployees) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return mapToDepartmentResponseDTO(department, expandEmployees);
    }

    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentUpdateRequestDTO requestDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        if (!existingDepartment.getName().equals(requestDTO.getName()) && departmentRepository.existsByName(requestDTO.getName())) {
            throw new ValidationException("Department with name '" + requestDTO.getName() + "' already exists.");
        }

        existingDepartment.setName(requestDTO.getName());
        existingDepartment.setCreationDate(requestDTO.getCreationDate());

        if (requestDTO.getDepartmentHeadId() != null) {
            Employee newDepartmentHead = employeeRepository.findById(requestDTO.getDepartmentHeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + requestDTO.getDepartmentHeadId()));
            existingDepartment.setDepartmentHead(newDepartmentHead);
        } else {
            existingDepartment.setDepartmentHead(null); // Allow unsetting department head
        }

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return mapToDepartmentResponseDTO(updatedDepartment, false);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        long employeeCount = employeeRepository.countByDepartmentId(id);
        if (employeeCount > 0) {
            throw new ValidationException("Cannot delete department as there are " + employeeCount + " employees assigned to it.");
        }
        departmentRepository.delete(department);
    }

    // --- Mappers ---
    private DepartmentResponseDTO mapToDepartmentResponseDTO(Department department, boolean expandEmployees) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setCreationDate(department.getCreationDate());

        if (department.getDepartmentHead() != null) {
            dto.setDepartmentHead(new EmployeeLookupDTO(department.getDepartmentHead().getId(), department.getDepartmentHead().getName()));
        }

        if (expandEmployees && department.getEmployees() != null) {
            // Ensure employees are loaded (lazy loading might require this depending on context)
            department.getEmployees().size(); // Trigger initialization of the collection if lazily loaded
            dto.setEmployees(department.getEmployees().stream()
                    .map(this::mapEmployeeToEmployeeResponseDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    // Helper method for mapping Employee to EmployeeResponseDTO
    // Duplicated from EmployeeService for now, can be refactored into a separate mapper utility
    private EmployeeResponseDTO mapEmployeeToEmployeeResponseDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setSalary(employee.getSalary());
        dto.setAddress(employee.getAddress());
        dto.setRole(employee.getRole());
        dto.setJoiningDate(employee.getJoiningDate());
        dto.setYearlyBonusPercentage(employee.getYearlyBonusPercentage());

        if (employee.getDepartment() != null) {
            dto.setDepartment(new DepartmentLookupDTO(employee.getDepartment().getId(), employee.getDepartment().getName()));
        }
        if (employee.getReportingManager() != null) {
            dto.setReportingManager(new EmployeeLookupDTO(employee.getReportingManager().getId(), employee.getReportingManager().getName()));
        }
        return dto;
    }
}