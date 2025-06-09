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
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository; // To link employees to departments

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeCreateRequestDTO requestDTO) {
        Employee employee = new Employee();
        employee.setName(requestDTO.getName());
        employee.setDateOfBirth(requestDTO.getDateOfBirth());
        employee.setSalary(requestDTO.getSalary());
        employee.setAddress(requestDTO.getAddress());
        employee.setRole(requestDTO.getRole());
        employee.setJoiningDate(requestDTO.getJoiningDate());
        employee.setYearlyBonusPercentage(requestDTO.getYearlyBonusPercentage());

        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + requestDTO.getDepartmentId()));
            employee.setDepartment(department);
            department.getEmployees().add(employee); // Add to department's employee set
        }

        if (requestDTO.getReportingManagerId() != null) {
            Employee reportingManager = employeeRepository.findById(requestDTO.getReportingManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reporting Manager not found with ID: " + requestDTO.getReportingManagerId()));
            employee.setReportingManager(reportingManager);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToEmployeeResponseDTO(savedEmployee);
    }

    public PagedResponse<EmployeeResponseDTO> getAllEmployees(int page, int size, boolean lookup) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<?> content;
        if (lookup) {
            content = employeePage.getContent().stream()
                    .map(this::mapToEmployeeLookupDTO)
                    .collect(Collectors.toList());
        } else {
            content = employeePage.getContent().stream()
                    .map(this::mapToEmployeeResponseDTO)
                    .collect(Collectors.toList());
        }

        return new PagedResponse<>(
                (List<EmployeeResponseDTO>) content, // Cast is safe because of the conditional mapping
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast(),
                employeePage.isFirst()
        );
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return mapToEmployeeResponseDTO(employee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeUpdateRequestDTO requestDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        if (requestDTO.getName() != null) {
            existingEmployee.setName(requestDTO.getName());
        }
        if (requestDTO.getDateOfBirth() != null) {
            existingEmployee.setDateOfBirth(requestDTO.getDateOfBirth());
        }
        if (requestDTO.getSalary() != null) {
            existingEmployee.setSalary(requestDTO.getSalary());
        }
        if (requestDTO.getAddress() != null) {
            existingEmployee.setAddress(requestDTO.getAddress());
        }
        if (requestDTO.getRole() != null) {
            existingEmployee.setRole(requestDTO.getRole());
        }
        if (requestDTO.getJoiningDate() != null) {
            existingEmployee.setJoiningDate(requestDTO.getJoiningDate());
        }
        if (requestDTO.getYearlyBonusPercentage() != null) {
            existingEmployee.setYearlyBonusPercentage(requestDTO.getYearlyBonusPercentage());
        }

        if (requestDTO.getDepartmentId() != null) {
            Department newDepartment = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("New department not found with ID: " + requestDTO.getDepartmentId()));
            // Remove from old department's employee set if exists
            if (existingEmployee.getDepartment() != null) {
                existingEmployee.getDepartment().getEmployees().remove(existingEmployee);
            }
            existingEmployee.setDepartment(newDepartment);
            newDepartment.getEmployees().add(existingEmployee); // Add to new department's employee set
        } else if (requestDTO.getDepartmentId() == null && existingEmployee.getDepartment() != null) {
            // If departmentId is explicitly set to null, remove from current department
            existingEmployee.getDepartment().getEmployees().remove(existingEmployee);
            existingEmployee.setDepartment(null);
        }

        if (requestDTO.getReportingManagerId() != null) {
            if (id.equals(requestDTO.getReportingManagerId())) {
                throw new ValidationException("An employee cannot be their own reporting manager.");
            }
            Employee newReportingManager = employeeRepository.findById(requestDTO.getReportingManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("New reporting manager not found with ID: " + requestDTO.getReportingManagerId()));
            existingEmployee.setReportingManager(newReportingManager);
        } else if (requestDTO.getReportingManagerId() == null && existingEmployee.getReportingManager() != null) {
            // If reportingManagerId is explicitly set to null, remove current reporting manager
            existingEmployee.setReportingManager(null);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapToEmployeeResponseDTO(updatedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployeeDepartment(Long employeeId, EmployeeUpdateDepartmentRequestDTO requestDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Department newDepartment = departmentRepository.findById(requestDTO.getNewDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("New department not found with ID: " + requestDTO.getNewDepartmentId()));

        // Remove from old department's employee set if exists
        if (employee.getDepartment() != null) {
            employee.getDepartment().getEmployees().remove(employee);
        }

        employee.setDepartment(newDepartment);
        newDepartment.getEmployees().add(employee); // Add to new department's employee set

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToEmployeeResponseDTO(updatedEmployee);
    }


    // --- Mappers ---
    private EmployeeResponseDTO mapToEmployeeResponseDTO(Employee employee) {
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

    private EmployeeLookupDTO mapToEmployeeLookupDTO(Employee employee) {
        return new EmployeeLookupDTO(employee.getId(), employee.getName());
    }
}