package com.example.employeemanagementsystem.repository;

import com.example.employeemanagementsystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Custom query to fetch employees by department ID, potentially with expanded department info
    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

    // Count employees in a department
    long countByDepartmentId(Long departmentId);
}