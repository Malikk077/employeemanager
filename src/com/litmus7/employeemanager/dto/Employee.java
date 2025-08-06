package com.litmus7.employeemanager.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Employee {
	private int employeeId ;
	private String firstName ;
	private String lastName ;
	private String email ;
	private String phone ;
	private String department ;
	private Double salary ;
	private Date joinDate ;
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
	// Overloaded method for LocalDate
	public void setJoinDate(LocalDate localDate) {
	    if (localDate != null) {
	        this.joinDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	    } else {
	        this.joinDate = null;
	    }
	}
	public String toDetailedString() {
	        return 
	               "employeeId=" + this.getEmployeeId() +
	               ", firstName='" + this.getFirstName() + '\'' +
	               ", lastName='" + this.getLastName() + '\'' +
	               ", email='" + this.getEmail() + '\'' +
	               ", phone='" + this.getPhone() + '\'' +
	               ", department='" + this.getDepartment() + '\'' 	               
//	               +", salary=" + this.getSalary() +
//	               ", joinDate=" + this.getJoinDate()
	               ;
	    }
	
	
	
}
	  
	    