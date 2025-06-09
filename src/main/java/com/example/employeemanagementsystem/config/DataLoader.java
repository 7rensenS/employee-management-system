package com.example.employeemanagementsystem.config;

import com.example.employeemanagementsystem.entity.Department;
import com.example.employeemanagementsystem.entity.Employee;
import com.example.employeemanagementsystem.repository.DepartmentRepository;
import com.example.employeemanagementsystem.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
public class DataLoader {

    @Bean
    @Transactional
    CommandLineRunner initDatabase(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        return args -> {
            if (departmentRepository.count() > 0 || employeeRepository.count() > 0) {
                System.out.println("Database already contains data. Skipping initial load.");
                return;
            }

            System.out.println("Loading initial data...");

            // --- 1. Create and Save Departments First (without heads initially) ---
            // Use no-args constructor and setters
            Department hr = new Department();
            hr.setName("Human Resources");
            hr.setCreationDate(LocalDate.of(2020, 1, 15));
            hr = departmentRepository.save(hr);

            Department engineering = new Department();
            engineering.setName("Engineering");
            engineering.setCreationDate(LocalDate.of(2019, 5, 20));
            engineering = departmentRepository.save(engineering);

            Department sales = new Department();
            sales.setName("Sales");
            sales.setCreationDate(LocalDate.of(2021, 3, 10));
            sales = departmentRepository.save(sales);

            Department marketing = new Department();
            marketing.setName("Marketing");
            marketing.setCreationDate(LocalDate.of(2022, 1, 1));
            marketing = departmentRepository.save(marketing);

            Department finance = new Department();
            finance.setName("Finance");
            finance.setCreationDate(LocalDate.of(2018, 7, 25));
            finance = departmentRepository.save(finance);

            List<Department> departments = new ArrayList<>(List.of(hr, engineering, sales, marketing, finance));

            Random random = new Random();
            List<Employee> employees = new ArrayList<>();

            // --- 2. Create and Save Top-Level Employees (CEOs/Directors) ---
            // Use no-args constructor and setters
            Employee ceo = new Employee();
            ceo.setName("Alice Smith");
            ceo.setDateOfBirth(LocalDate.of(1980, 1, 1));
            ceo.setSalary(new BigDecimal("200000.00"));
            ceo.setAddress("123 Main St");
            ceo.setRole("CEO");
            ceo.setJoiningDate(LocalDate.of(2010, 1, 1));
            ceo.setYearlyBonusPercentage(10.0);
            // No department or reporting manager for CEO initially
            ceo = employeeRepository.save(ceo);
            employees.add(ceo);

            // CTO
            Employee cto = new Employee();
            cto.setName("Bob Johnson");
            cto.setDateOfBirth(LocalDate.of(1982, 3, 10));
            cto.setSalary(new BigDecimal("180000.00"));
            cto.setDepartment(engineering); // Assign department object directly
            cto.setAddress("456 Oak Ave");
            cto.setRole("CTO");
            cto.setJoiningDate(LocalDate.of(2012, 6, 1));
            cto.setYearlyBonusPercentage(9.0);
            cto.setReportingManager(ceo); // Assign persisted CEO object
            cto = employeeRepository.save(cto);
            employees.add(cto);
            // Add employee to department's collection (important for bidirectional)
            engineering.getEmployees().add(cto);

            // HR Director
            Employee hrDirector = new Employee();
            hrDirector.setName("Carol White");
            hrDirector.setDateOfBirth(LocalDate.of(1978, 7, 5));
            hrDirector.setSalary(new BigDecimal("150000.00"));
            hrDirector.setDepartment(hr); // Assign department object directly
            hrDirector.setAddress("789 Pine Ln");
            hrDirector.setRole("HR Director");
            hrDirector.setJoiningDate(LocalDate.of(2015, 2, 1));
            hrDirector.setYearlyBonusPercentage(8.0);
            hrDirector.setReportingManager(ceo); // Assign persisted CEO object
            hrDirector = employeeRepository.save(hrDirector);
            employees.add(hrDirector);
            // Add employee to department's collection (important for bidirectional)
            hr.getEmployees().add(hrDirector);

            // --- 3. Assign Department Heads to already saved departments ---
            hr.setDepartmentHead(hrDirector);
            engineering.setDepartmentHead(cto);
            departmentRepository.save(hr); // Update HR department (with head)
            departmentRepository.save(engineering); // Update Engineering department (with head)

            // --- 4. Create 22+ more employees, assigning them to departments and managers (which are already persisted) ---
            String[] roles = {"Manager", "Senior Developer", "Junior Developer", "Analyst", "Associate", "Sales Rep", "Marketing Specialist"};
            for (int i = 0; i < 22; i++) {
                Employee emp = new Employee();
                emp.setName("Employee " + (i + 1));
                emp.setDateOfBirth(LocalDate.of(1990 + random.nextInt(10), 1 + random.nextInt(12), 1 + random.nextInt(28)));
                emp.setSalary(BigDecimal.valueOf(50000 + random.nextInt(100000)));
                emp.setAddress(UUID.randomUUID().toString().substring(0, 8) + " St");
                emp.setRole(roles[random.nextInt(roles.length)]);
                emp.setJoiningDate(LocalDate.of(2020 + random.nextInt(4), 1 + random.nextInt(12), 1 + random.nextInt(28)));
                emp.setYearlyBonusPercentage(2.0 + random.nextDouble() * 8.0);

                Department dept = departments.get(random.nextInt(departments.size()));
                emp.setDepartment(dept); // Set Department object directly
                dept.getEmployees().add(emp); // Add to department's collection

                Employee reportingTo = null;
                if (!employees.isEmpty()) {
                    reportingTo = employees.get(random.nextInt(employees.size()));
                }
                emp.setReportingManager(reportingTo); // Set Employee object directly

                emp = employeeRepository.save(emp);
                employees.add(emp);
            }
            System.out.println("Initial data loaded successfully: " + employees.size() + " employees and " + departments.size() + " departments.");
        };
    }
}