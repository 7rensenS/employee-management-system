package com.example.employeemanagementsystem.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private LocalDate creationDate;
    private EmployeeLookupDTO departmentHead; // Using lookup DTO for simplicity
    private List<EmployeeResponseDTO> employees; // For expand=employee

    public DepartmentResponseDTO(Long id, String name, LocalDate creationDate, EmployeeLookupDTO departmentHead) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.departmentHead = departmentHead;
    }
}