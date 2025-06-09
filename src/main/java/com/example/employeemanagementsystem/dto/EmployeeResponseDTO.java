package com.example.employeemanagementsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private BigDecimal salary;
    private DepartmentLookupDTO department; // Simplified department info
    private String address;
    private String role;
    private LocalDate joiningDate;
    private Double yearlyBonusPercentage;
    private EmployeeLookupDTO reportingManager; // Simplified reporting manager info
}