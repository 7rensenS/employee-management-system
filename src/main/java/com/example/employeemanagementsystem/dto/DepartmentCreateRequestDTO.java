package com.example.employeemanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateRequestDTO {
    @NotBlank(message = "Department name is required")
    private String name;

    @NotNull(message = "Creation date is required")
    private LocalDate creationDate;

    // Optional: Department head ID can be provided during creation
    private Long departmentHeadId;
}