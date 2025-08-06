package com.litmus7.employeemanager.constant;

public class SQLConstants {
  public static final String INSERT_TO_EMPLOYEES="INSERT INTO employees (emp_id, first_name, last_name, email, phone, department, salary, join_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String SELECT_1_FROM_EMPLOYEES="select 1 from employees where emp_id = ?";
	public static final String SELECT_ALL_EMPLOYEES="select emp_id,first_name, last_name, email, phone,department From Employees";
	
	public static final String EMP_ID="emp_id";
	public static final String FIRST_NAME="first_name";
	public static final String LAST_NAME="last_name";
	public static final String EMAIL="email";
	public static final String PHONE="phone";
	public static final String DEPARTMENT="department";
	public static final String SALARY="salary";
	public static final String JOIN_DATE="join_date";
	
	
	
}
