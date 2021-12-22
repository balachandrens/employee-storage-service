package com.ebf.employeeservice.repository;

import com.ebf.employeeservice.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
