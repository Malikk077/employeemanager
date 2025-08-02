package com.litmus7.employeemanager.constants;

public class SQLConstants {
  public static final String INSERT_TO_EMPLOYEES="INSERT INTO employees (emp_id, first_name, last_name, email, phone, department, salary, join_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String SELECT_1_FROM_EMPLOYEES="select 1 from employees where emp_id = ?";
	public static final String SELECT_ALL_EMPLOYEES="select * From Employees";
}
