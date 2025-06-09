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
    git clone [https://github.com/](https://github.com/)<your-username>/employee-management-system.git
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
📝 API Endpoints Summary
Here's a quick overview of the main API endpoints. Refer to the Swagger UI for detailed request/response schemas and testing.

Departments
POST /api/departments - Create a new department.
GET /api/departments - Get all departments (paginated).
Query Parameter: ?page={pageNo}&size={pageSize}
Query Parameter: ?expand=employee (to include assigned employees)
GET /api/departments/{id} - Get a department by ID.
Path Variable: {id} (department ID)
Query Parameter: ?expand=employee (to include assigned employees)
PUT /api/departments/{id} - Update department details.
Path Variable: {id} (department ID)
DELETE /api/departments/{id} - Delete a department.
Path Variable: {id} (department ID)
Employees
POST /api/employees - Create a new employee.
GET /api/employees - Get all employees (paginated).
Query Parameter: ?page={pageNo}&size={pageSize}
Query Parameter: ?lookup=true (to list only names and IDs)
GET /api/employees/{id} - Get an employee by ID.
Path Variable: {id} (employee ID)
PUT /api/employees/{id} - Update employee details.
Path Variable: {id} (employee ID)
PATCH /api/employees/{id}/department - Update an employee's department.
Path Variable: {id} (employee ID)
