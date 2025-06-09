package com.example.employeemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(name = "creation_date", nullable = false)
	private LocalDate creationDate;

	// Department head can be null initially, but should link to an existing
	// employee
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "head_employee_id")
	private Employee departmentHead;

	@JsonIgnore // Prevent infinite recursion when fetching departments that also fetch
				// employees
	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Employee> employees = new HashSet<>();

	@PrePersist
	protected void onCreate() {
		if (creationDate == null) {
			creationDate = LocalDate.now();
		}
	}
}