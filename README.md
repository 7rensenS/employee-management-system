# Employee Management System API

Welcome to the **Employee Management System (EMS)** API! This Spring Boot application provides a comprehensive RESTful platform for managing employees and departments, designed to meet the requirements of your technical assessment.

The goal was to build a robust, scalable, and maintainable API service with proper error handling, validation, and adherence to modern Spring Boot development practices.

---

## 🚀 Features Implemented

This API covers all specified functionalities, including:

* **Employee Management:**
    * **Create Employee:** Add new employees with detailed information (name, date of birth, salary, department, address, role/title, joining date, yearly bonus percentage, and reporting manager).
    * **Update Employee:** Modify an existing employee's details.
    * **Move Employee Department:** Reassign an employee to a different department.
    * **Fetch All Employees:** Retrieve a paginated list of all employees.
    * **Fetch Employee by ID:** Get detailed information for a specific employee.
    * **List Employee Names & IDs:** Special endpoint to list only names and IDs triggered by passing `lookup=true` as a query parameter (`/api/employees?lookup=true`).
* **Department Management:**
    * **Add Department:** Create new departments with a name, creation date, and an optional department head.
    * **Update Department:** Modify existing department details.
    * **Delete Department:** Remove a department. This operation intelligently fails if there are employees currently assigned to the department, ensuring data integrity.
    * **Fetch All Departments:** Retrieve a paginated list of all departments.
    * **Fetch Department by ID:** Get details for a specific department.
    * **Expand Employees under Departments:** Retrieve a department's details along with its list of assigned employees when `expand=employee` is provided as a query parameter (`/api/departments/{id}?expand=employee` or `/api/departments?expand=employee`).
* **Data Integrity & Robustness:**
    * **Input Validation:** Robust validation of all incoming API request payloads using Jakarta Bean Validation (`@Valid` annotations and constraints like `@NotBlank`, `@NotNull`, `@DecimalMin`, `@PastOrPresent`).
    * **Global Exception Handling:** Centralized, consistent error responses for `ResourceNotFoundException`, `ValidationException`, and other unhandled exceptions using `@ControllerAdvice`.
    * **Complex Logic Handling:** Implemented logic to prevent department deletion if employees are assigned. The self-referencing `reportingManager` relationship is correctly handled.
* **Performance & Scalability:**
    * **Pagination:** All `GET` APIs are by default paginated with **20 items per page**. The API response for paginated endpoints includes the current page number, total number of elements, and total number of pages for efficient client-side handling.
* **Clear Architecture:** Adherence to a standard Spring Boot layered architecture (`Controller` → `Service` → `Repository`), promoting maintainability and separation of concerns.
* **JPA Implementation:** Utilizes Spring Data JPA for seamless database interaction with defined entities (`Department`, `Employee`) and their corresponding repositories.
* **Bidirectional Relationship Management:** Careful handling of `@OneToMany` and `@ManyToOne` relationships to ensure data consistency and prevent infinite recursion during JSON serialization.
* **Code Quality:** Use of collections, appropriate data types, and clear conditional logic as per best practices.

---
## 🛠️ Technologies Used

* **Java 17+**: The core programming language.
* **Spring Boot 3.5.x**: The framework for building robust, stand-alone, production-grade Spring applications.
* **Spring Data JPA**: For simplified data access and persistence with Hibernate as the JPA provider.
* **H2 Database**: An in-memory database used for development and testing, easily swappable with external databases like PostgreSQL or MySQL.
* **Maven**: Dependency management and build automation tool.
* **Lombok**: Reduces boilerplate code (getters, setters, constructors).
* **Jakarta Validation (Bean Validation)**: For declarative validation of input data.

---

## ⚙️ Setup and Run

Follow these steps to get the application up and running on your local machine:

1.  **Prerequisites:**
    * **Java Development Kit (JDK) 17 or higher** installed.
    * **Maven** installed (usually comes bundled with modern IDEs like IntelliJ IDEA).
    * A good **Integrated Development Environment (IDE)** like IntelliJ IDEA (recommended), VS Code, or Eclipse.
2.  **Clone the Repository:**
    ```bash
    git clone https://github.com/7rensenS/employee-management-system.git
    cd employee-management-system
    ```
    *(Remember to replace `<your-username>` with your actual GitHub username where you've pushed the code.)*
3.  **Build the Project:**
    Navigate to the project root directory in your terminal and run the Maven build command:
    ```bash
    mvn clean install
    ```
    This command compiles the code, runs tests, and packages the application.
4.  **Run the Application:**
    You can run the Spring Boot application using Maven:
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the `EmployeeManagementSystemApplication.java` file directly from your IDE.

    The application will start on `http://localhost:8080`.

---

## 💾 Database Details

This project uses an **H2 in-memory database** for simplicity and ease of setup during development and assessment. The database schema is automatically created on startup by Spring Data JPA based on the defined entities (`ddl-auto=update` configured in `application.properties`).

### Database Structure

The core entities are `Department` and `Employee`, mapped to `departments` and `employees` tables respectively.

| Column Name               | Data Type         | Constraints                                  | Description                                    |
| :------------------------ | :---------------- | :------------------------------------------- | :--------------------------------------------- |
| `id`                      | `BIGINT`          | `PRIMARY KEY`, `AUTO_INCREMENT`              | Unique identifier for the entity               |
| `name`                    | `VARCHAR(255)`    | `NOT NULL`, `UNIQUE` (for Department)        | Name of the department or employee             |
| `creation_date`           | `DATE`            | `NOT NULL` (for Department)                  | Date the department was established            |
| `head_employee_id`        | `BIGINT`          | `FOREIGN KEY REFERENCES employees(id)` (nullable) | ID of the employee designated as department head |
| `date_of_birth`           | `DATE`            | `NOT NULL` (for Employee)                    | Employee's date of birth                       |
| `salary`                  | `DECIMAL(19,2)`   | `NOT NULL` (for Employee)                    | Employee's yearly salary                       |
| `department_id`           | `BIGINT`          | `FOREIGN KEY REFERENCES departments(id)` (nullable) | ID of the department the employee belongs to   |
| `address`                 | `VARCHAR(255)`    |                                              | Employee's residential address                 |
| `role`                    | `VARCHAR(255)`    | `NOT NULL` (for Employee)                    | Employee's job role or title                   |
| `joining_date`            | `DATE`            | `NOT NULL` (for Employee)                    | Date the employee joined the company           |
| `yearly_bonus_percentage` | `DOUBLE`          | `NOT NULL` (for Employee)                    | Percentage of yearly bonus (e.g., 5.0 for 5%)  |
| `reporting_manager_id`    | `BIGINT`          | `FOREIGN KEY REFERENCES employees(id)` (nullable) | ID of the employee's reporting manager         |

*(A `.sql` script for creating these tables is not explicitly provided in the repository, as Spring Data JPA automatically generates the schema based on entities for H2. However, the structure is clearly defined above for reference.)*

### H2 Console

You can access the H2 database console to inspect the data directly:

➡️ **[http://localhost:8080/h2-console](http://localhost:8080/h2-console)**

Use the following credentials to connect:
* **JDBC URL:** `jdbc:h2:mem:employeedb`
* **User Name:** `sa`
* **Password:** `password`

### Initial Data

Upon application startup, the `DataLoader` class (`com.example.employeemanagementsystem.config.DataLoader`) automatically populates the H2 database with essential sample data, demonstrating the relationships:
* At least **3 departments** are created.
* At least **25 employees** are generated, including top-level employees, employees assigned to departments, and employees with reporting managers.
This allows for immediate testing of all API endpoints without manual data creation.

---

## ⚠️ Error Handling

The API provides clear and consistent error responses using a global exception handler (`GlobalExceptionHandler`). Common error scenarios are mapped to appropriate HTTP status codes:

* **`404 Not Found`**: When a requested resource (employee or department) does not exist.
* **`400 Bad Request`**: For invalid input (e.g., missing required fields, invalid formats due to `@Valid` annotations) or business rule violations (e.g., attempting to delete a department with active employees).
* **`500 Internal Server Error`**: For unexpected server-side issues.

**Example Error Response Body:**

```json
{
  "timestamp": "YYYY-MM-DDTHH:MM:SS.SSSSSS",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with ID: 100",
  "path": "/api/employees/100"
}

---

## 📝 API Endpoints Detailed Documentation

All endpoints are served under the base URL `http://localhost:8080`.

---

### **1. Department Endpoints**

#### **1.1. Create a New Department**

* **Purpose:** To add a new department to the system.
* **URL:** `/api/departments`
* **Method:** `POST`
* **Request Body (`application/json`):** `DepartmentCreateRequestDTO`
    ```json
    {
      "name": "Quality Assurance",
      "creationDate": "2023-11-01",
      "departmentHeadId": 50 // Optional: ID of an existing employee to be the head
    }
    ```
* **Response Body (`application/json`):** `DepartmentResponseDTO` (HTTP Status: `201 Created`)
    ```json
    {
      "id": 101,
      "name": "Quality Assurance",
      "creationDate": "2023-11-01",
      "departmentHead": {
        "id": 50,
        "name": "John Doe",
        "role": "QA Lead"
      }
    }
    ```
* **Error Responses:**
    * `400 Bad Request`: If `name` or `creationDate` are missing/invalid, or if `departmentHeadId` refers to a non-existent employee.
    * `409 Conflict`: If a department with the given `name` already exists.

#### **1.2. Get All Departments**

* **Purpose:** To retrieve a paginated list of all departments. Can optionally expand to include associated employees.
* **URL:** `/api/departments`
* **Method:** `GET`
* **Query Parameters:**
    * `page` (optional): `int`, default `0`. The page number to retrieve (0-indexed).
    * `size` (optional): `int`, default `20`. The number of items per page.
    * `expand` (optional): `String`, set to `"employee"` to include a list of employees in each department's response.
* **Response Body (`application/json`):** `PagedResponse<DepartmentResponseDTO>`
    *(Without `expand=employee`)*
    ```json
    {
      "content": [
        {
          "id": 1,
          "name": "Human Resources",
          "creationDate": "2020-01-15",
          "departmentHead": {
            "id": 3,
            "name": "Carol White",
            "role": "HR Director"
          }
        },
        {
          "id": 2,
          "name": "Engineering",
          "creationDate": "2019-05-20",
          "departmentHead": {
            "id": 2,
            "name": "Bob Johnson",
            "role": "CTO"
          }
        }
      ],
      "pageNo": 0,
      "pageSize": 20,
      "totalElements": 5,
      "totalPages": 1,
      "last": true
    }
    ```
    *(With `expand=employee`)*
    ```json
    {
      "content": [
        {
          "id": 1,
          "name": "Human Resources",
          "creationDate": "2020-01-15",
          "departmentHead": {
            "id": 3,
            "name": "Carol White",
            "role": "HR Director"
          },
          "employees": [
            {
              "id": 3,
              "name": "Carol White",
              "dateOfBirth": "1978-07-05",
              "salary": 150000.00,
              "address": "789 Pine Ln",
              "role": "HR Director",
              "joiningDate": "2015-02-01",
              "yearlyBonusPercentage": 8.0,
              "reportingManagerName": "Alice Smith"
            },
            {
              "id": 10,
              "name": "Employee 7",
              "dateOfBirth": "1994-09-20",
              "salary": 85000.00,
              "address": "c8d45f44 St",
              "role": "Associate",
              "joiningDate": "2022-07-10",
              "yearlyBonusPercentage": 5.5,
              "reportingManagerName": "Carol White"
            }
            // ... more employees
          ]
        }
        // ... more departments
      ],
      "pageNo": 0,
      "pageSize": 20,
      "totalElements": 5,
      "totalPages": 1,
      "last": true
    }
    ```

#### **1.3. Get Department by ID**

* **Purpose:** To retrieve details for a specific department by its ID. Can optionally expand to include associated employees.
* **URL:** `/api/departments/{id}`
* **Method:** `GET`
* **Path Parameter:** `{id}`: `long`, The ID of the department.
* **Query Parameter:**
    * `expand` (optional): `String`, set to `"employee"` to include a list of employees under this department.
* **Response Body (`application/json`):** `DepartmentResponseDTO` (HTTP Status: `200 OK`)
    *(Without `expand=employee`)*
    ```json
    {
      "id": 1,
      "name": "Human Resources",
      "creationDate": "2020-01-15",
      "departmentHead": {
        "id": 3,
        "name": "Carol White",
        "role": "HR Director"
      }
    }
    ```
    *(With `expand=employee`)*
    ```json
    {
      "id": 1,
      "name": "Human Resources",
      "creationDate": "2020-01-15",
      "departmentHead": {
        "id": 3,
        "name": "Carol White",
        "role": "HR Director"
      },
      "employees": [
        {
          "id": 3,
          "name": "Carol White",
          "dateOfBirth": "1978-07-05",
          "salary": 150000.00,
          "address": "789 Pine Ln",
          "role": "HR Director",
          "joiningDate": "2015-02-01",
          "yearlyBonusPercentage": 8.0,
          "reportingManagerName": "Alice Smith"
        },
        {
          "id": 10,
          "name": "Employee 7",
          "dateOfBirth": "1994-09-20",
          "salary": 85000.00,
          "address": "c8d45f44 St",
          "role": "Associate",
          "joiningDate": "2022-07-10",
          "yearlyBonusPercentage": 5.5,
          "reportingManagerName": "Carol White"
        }
      ]
    }
    ```
* **Error Responses:**
    * `404 Not Found`: If no department exists with the given ID.

#### **1.4. Update Department Details**

* **Purpose:** To modify details of an existing department.
* **URL:** `/api/departments/{id}`
* **Method:** `PUT`
* **Path Parameter:** `{id}`: `long`, The ID of the department to update.
* **Request Body (`application/json`):** `DepartmentUpdateRequestDTO`
    ```json
    {
      "name": "HR Operations",
      "creationDate": "2020-01-15", // Can be the same or updated
      "departmentHeadId": 51 // Optional: New department head ID, null to remove head
    }
    ```
* **Response Body (`application/json`):** `DepartmentResponseDTO` (HTTP Status: `200 OK`)
    ```json
    {
      "id": 1,
      "name": "HR Operations",
      "creationDate": "2020-01-15",
      "departmentHead": {
        "id": 51,
        "name": "Jane Doe",
        "role": "HR Manager"
      }
    }
    ```
* **Error Responses:**
    * `400 Bad Request`: If request body is invalid or `departmentHeadId` refers to a non-existent employee.
    * `404 Not Found`: If no department exists with the given ID.
    * `409 Conflict`: If the new `name` conflicts with an existing department name.

#### **1.5. Delete a Department**

* **Purpose:** To remove a department from the system. Fails if employees are assigned.
* **URL:** `/api/departments/{id}`
* **Method:** `DELETE`
* **Path Parameter:** `{id}`: `long`, The ID of the department to delete.
* **Response Body:** (No Content, HTTP Status: `204 No Content`)
* **Error Responses:**
    * `404 Not Found`: If no department exists with the given ID.
    * `400 Bad Request`: If the department has associated employees (e.g., `message: "Cannot delete department with ID: X as it still has assigned employees."`).

---

### **2. Employee Endpoints**

#### **2.1. Create a New Employee**

* **Purpose:** To add a new employee to the system.
* **URL:** `/api/employees`
* **Method:** `POST`
* **Request Body (`application/json`):** `EmployeeCreateRequestDTO`
    ```json
    {
      "name": "Michael Brown",
      "dateOfBirth": "1995-03-22",
      "salary": 75000.00,
      "departmentId": 2,             // Optional: ID of an existing department
      "address": "101 Elm St",
      "role": "Junior Developer",
      "joiningDate": "2024-01-15",
      "yearlyBonusPercentage": 5.0,
      "reportingManagerId": 2        // Optional: ID of an existing employee (manager)
    }
    ```
* **Response Body (`application/json`):** `EmployeeResponseDTO` (HTTP Status: `201 Created`)
    ```json
    {
      "id": 105,
      "name": "Michael Brown",
      "dateOfBirth": "1995-03-22",
      "salary": 75000.00,
      "department": {
        "id": 2,
        "name": "Engineering"
      },
      "address": "101 Elm St",
      "role": "Junior Developer",
      "joiningDate": "2024-01-15",
      "yearlyBonusPercentage": 5.0,
      "reportingManager": {
        "id": 2,
        "name": "Bob Johnson",
        "role": "CTO"
      },
      "reportingManagerName": "Bob Johnson"
    }
    ```
* **Error Responses:**
    * `400 Bad Request`: If any required fields are missing/invalid, `departmentId` or `reportingManagerId` refer to non-existent entities.
    * `400 Bad Request`: If `reportingManagerId` is the same as the employee being created (self-referencing logic error).

#### **2.2. Get All Employees**

* **Purpose:** To retrieve a paginated list of all employees. Can optionally return only names and IDs.
* **URL:** `/api/employees`
* **Method:** `GET`
* **Query Parameters:**
    * `page` (optional): `int`, default `0`. The page number to retrieve (0-indexed).
    * `size` (optional): `int`, default `20`. The number of items per page.
    * `lookup` (optional): `boolean`, set to `true` to return `EmployeeLookupDTO` (only `id` and `name`).
* **Response Body (`application/json`):** `PagedResponse<EmployeeResponseDTO>` or `PagedResponse<EmployeeLookupDTO>`
    *(Without `lookup=true`)*
    ```json
    {
      "content": [
        {
          "id": 1,
          "name": "Alice Smith",
          "dateOfBirth": "1980-01-01",
          "salary": 200000.00,
          "department": null,
          "address": "123 Main St",
          "role": "CEO",
          "joiningDate": "2010-01-01",
          "yearlyBonusPercentage": 10.0,
          "reportingManager": null,
          "reportingManagerName": null
        },
        {
          "id": 2,
          "name": "Bob Johnson",
          "dateOfBirth": "1982-03-10",
          "salary": 180000.00,
          "department": {
            "id": 2,
            "name": "Engineering"
          },
          "address": "456 Oak Ave",
          "role": "CTO",
          "joiningDate": "2012-06-01",
          "yearlyBonusPercentage": 9.0,
          "reportingManager": {
            "id": 1,
            "name": "Alice Smith",
            "role": "CEO"
          },
          "reportingManagerName": "Alice Smith"
        }
      ],
      "pageNo": 0,
      "pageSize": 20,
      "totalElements": 25,
      "totalPages": 2,
      "last": false
    }
    ```
    *(With `lookup=true`)*
    ```json
    {
      "content": [
        {
          "id": 1,
          "name": "Alice Smith"
        },
        {
          "id": 2,
          "name": "Bob Johnson"
        },
        {
          "id": 3,
          "name": "Carol White"
        }
        // ... more employees
      ],
      "pageNo": 0,
      "pageSize": 20,
      "totalElements": 25,
      "totalPages": 2,
      "last": false
    }
    ```

#### **2.3. Get Employee by ID**

* **Purpose:** To retrieve detailed information for a specific employee by their ID.
* **URL:** `/api/employees/{id}`
* **Method:** `GET`
* **Path Parameter:** `{id}`: `long`, The ID of the employee to retrieve.
* **Response Body (`application/json`):** `EmployeeResponseDTO` (HTTP Status: `200 OK`)
    ```json
    {
      "id": 2,
      "name": "Bob Johnson",
      "dateOfBirth": "1982-03-10",
      "salary": 180000.00,
      "department": {
        "id": 2,
        "name": "Engineering"
      },
      "address": "456 Oak Ave",
      "role": "CTO",
      "joiningDate": "2012-06-01",
      "yearlyBonusPercentage": 9.0,
      "reportingManager": {
        "id": 1,
        "name": "Alice Smith",
        "role": "CEO"
      },
      "reportingManagerName": "Alice Smith"
    }
    ```
* **Error Responses:**
    * `404 Not Found`: If no employee exists with the given ID.

#### **2.4. Update Employee Details**

* **Purpose:** To modify existing details of an employee.
* **URL:** `/api/employees/{id}`
* **Method:** `PUT`
* **Path Parameter:** `{id}`: `long`, The ID of the employee to update.
* **Request Body (`application/json`):** `EmployeeUpdateRequestDTO`
    ```json
    {
      "name": "Bob J. Johnson",
      "dateOfBirth": "1982-03-10",
      "salary": 190000.00,
      "departmentId": 2,             // Optional: New department ID, null to remove from department
      "address": "789 Willow Rd",
      "role": "Chief Technology Officer",
      "joiningDate": "2012-06-01",
      "yearlyBonusPercentage": 9.5,
      "reportingManagerId": 1        // Optional: New reporting manager ID, null to remove manager
    }
    ```
* **Response Body (`application/json`):** `EmployeeResponseDTO` (HTTP Status: `200 OK`)
    ```json
    {
      "id": 2,
      "name": "Bob J. Johnson",
      "dateOfBirth": "1982-03-10",
      "salary": 190000.00,
      "department": {
        "id": 2,
        "name": "Engineering"
      },
      "address": "789 Willow Rd",
      "role": "Chief Technology Officer",
      "joiningDate": "2012-06-01",
      "yearlyBonusPercentage": 9.5,
      "reportingManager": {
        "id": 1,
        "name": "Alice Smith",
        "role": "CEO"
      },
      "reportingManagerName": "Alice Smith"
    }
    ```
* **Error Responses:**
    * `400 Bad Request`: If request body is invalid, `departmentId` or `reportingManagerId` refer to non-existent entities, or `reportingManagerId` is the same as the employee's own ID.
    * `404 Not Found`: If no employee exists with the given ID.

#### **2.5. Update Employee's Department**

* **Purpose:** To move an employee from one department to another.
* **URL:** `/api/employees/{id}/department`
* **Method:** `PATCH`
* **Path Parameter:** `{id}`: `long`, The ID of the employee to update.
* **Request Body (`application/json`):** `EmployeeUpdateDepartmentRequestDTO`
    ```json
    {
      "departmentId": 4 // The ID of the new department, null to remove from current department
    }
    ```
* **Response Body (`application/json`):** `EmployeeResponseDTO` (HTTP Status: `200 OK`)
    ```json
    {
      "id": 5,
      "name": "Employee 2",
      "dateOfBirth": "1997-05-18",
      "salary": 65000.00,
      "department": {
        "id": 4,
        "name": "Marketing"
      },
      "address": "f3b9c1d2 St",
      "role": "Analyst",
      "joiningDate": "2023-03-01",
      "yearlyBonusPercentage": 4.0,
      "reportingManager": {
        "id": 2,
        "name": "Bob Johnson",
        "role": "CTO"
      },
      "reportingManagerName": "Bob Johnson"
    }
    ```
* **Error Responses:**
    * `400 Bad Request`: If `departmentId` is invalid or refers to a non-existent department.
    * `404 Not Found`: If no employee exists with the given ID.

---

### **3. DTO Schemas (Reference)**

These are the Data Transfer Objects (DTOs) used for API requests and responses. The actual JSON schema can be found in the Swagger UI.

#### **3.1. `DepartmentCreateRequestDTO`**

Used for `POST /api/departments`.
```json
{
  "name": "String",           // @NotBlank
  "creationDate": "YYYY-MM-DD", // @NotNull, @PastOrPresent
  "departmentHeadId": 0       // long, optional
}
---


### **3.2. `DepartmentUpdateRequestDTO`**


Used for `PUT /api/departments/{id}`.

```json
{
  "name": "String",           // @NotBlank
  "creationDate": "YYYY-MM-DD", // @NotNull, @PastOrPresent
  "departmentHeadId": 0       // long, optional (null to remove head)
}

### **3.3. `DepartmentResponseDTO`**

Used for responses for Department GET/POST/PUT.

JSON

{
  "id": 0,                    // long
  "name": "String",
  "creationDate": "YYYY-MM-DD",
  "departmentHead": {         // EmployeeLookupDTO, null if no head
    "id": 0,                  // long
    "name": "String",
    "role": "String"
  },
  "employees": [              // List<EmployeeResponseDTO>, only if ?expand=employee
    // ... EmployeeResponseDTO objects
  ]
}
### **3.4. `EmployeeCreateRequestDTO`**
Used for POST /api/employees.

JSON

{
  "name": "String",           // @NotBlank
  "dateOfBirth": "YYYY-MM-DD", // @NotNull, @Past
  "salary": 0.0,              // @NotNull, @DecimalMin(value = "0.0", inclusive = false)
  "departmentId": 0,          // long, optional
  "address": "String",
  "role": "String",           // @NotBlank
  "joiningDate": "YYYY-MM-DD", // @NotNull, @PastOrPresent
  "yearlyBonusPercentage": 0.0, // @NotNull, @DecimalMin(value = "0.0")
  "reportingManagerId": 0     // long, optional
}
### **3.5. `EmployeeUpdateRequestDTO`**
Used for PUT /api/employees/{id}.

JSON

{
  "name": "String",           // @NotBlank
  "dateOfBirth": "YYYY-MM-DD", // @NotNull, @Past
  "salary": 0.0,              // @NotNull, @DecimalMin(value = "0.0", inclusive = false)
  "departmentId": 0,          // long, optional (null to remove from department)
  "address": "String",
  "role": "String",           // @NotBlank
  "joiningDate": "YYYY-MM-DD", // @NotNull, @PastOrPresent
  "yearlyBonusPercentage": 0.0, // @NotNull, @DecimalMin(value = "0.0")
  "reportingManagerId": 0     // long, optional (null to remove manager)
}

### **3.6. `EmployeeUpdateDepartmentRequestDTO`**
Used for PATCH /api/employees/{id}/department.

JSON

{
  "departmentId": 0           // long, optional (null to remove from department)
}

### **3.7. `EmployeeResponseDTO`**
Used for responses for Employee GET/POST/PUT/PATCH.

JSON

{
  "id": 0,                    // long
  "name": "String",
  "dateOfBirth": "YYYY-MM-DD",
  "salary": 0.0,
  "department": {             // DepartmentLookupDTO, null if no department
    "id": 0,
    "name": "String"
  },
  "address": "String",
  "role": "String",
  "joiningDate": "YYYY-MM-DD",
  "yearlyBonusPercentage": 0.0,
  "reportingManager": {       // EmployeeLookupDTO, null if no manager
    "id": 0,
    "name": "String",
    "role": "String"
  },
  "reportingManagerName": "String" // Derived field for convenience
}

### **3.8. `EmployeeLookupDTO`**
Used when ?lookup=true for employees, or as a nested object in other DTOs.

JSON

{
  "id": 0,                    // long
  "name": "String"
}
(Note: when used as departmentHead or reportingManager in DepartmentResponseDTO and EmployeeResponseDTO respectively, the role field is also included).


### **3.9. `PagedResponse<T>`**
Generic wrapper for all paginated GET responses. T would be EmployeeResponseDTO, EmployeeLookupDTO, or DepartmentResponseDTO.

JSON

{
  "content": [
    // Array of T objects
  ],
  "pageNo": 0,                 // Current page number (0-indexed)
  "pageSize": 20,              // Items per page
  "totalElements": 0,          // Total number of elements across all pages
  "totalPages": 0,             // Total number of available pages
  "last": true                 // boolean, true if this is the last page
}
---
