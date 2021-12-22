package com.ebf.employeeservice.repository;

import com.ebf.employeeservice.model.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findAllByCompanyId(long companyId);
  Optional<Employee> findByIdAndCompanyId(long id, long companyId);
  @Query(value = "SELECT avg(salary) FROM Employee where company_id = ?1")
  Double averageSalaryByCompanyId(long companyId);
  void deleteByIdAndCompanyId(@Param("id") long id, @Param("company_id") long companyId);

}
