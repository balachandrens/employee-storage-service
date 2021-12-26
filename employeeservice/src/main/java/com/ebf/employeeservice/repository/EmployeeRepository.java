package com.ebf.employeeservice.repository;

import com.ebf.employeeservice.model.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findAllByCompanyId(long companyId);

  @Query(value = "SELECT avg(salary) FROM Employee where company_id = ?1")
  Double averageSalaryByCompanyId(long companyId);

}
