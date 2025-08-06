package com.litmus7.employeemanager.dao;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.constant.SQLConstants;
import com.litmus7.employeemanager.util.DBUtil;
import com.litmus7.employeemanager.exception.EmployeeDataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;



public class EmployeeDao 
{
	public boolean doesEmployeeExist(int id) throws EmployeeDataAccessException
	{
		
		try(Connection conn = DBUtil.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(SQLConstants.SELECT_1_FROM_EMPLOYEES);) 
		{
			stmt.setInt(1, id);
			try (ResultSet result = stmt.executeQuery()) {
	            return result.next(); 
	        }
		}
		 catch (SQLException e) 
		{
			 e.printStackTrace();
			 throw new EmployeeDataAccessException("SQL error on duplicate check", e);
		}
	}
	
	public boolean saveEmployee(Employee employee)  throws EmployeeDataAccessException 
	{
		

		try (Connection conn = DBUtil.getConnection();
		     PreparedStatement stmt = conn.prepareStatement(SQLConstants.INSERT_TO_EMPLOYEES)) 
		{

		    stmt.setInt(1, employee.getEmployeeId());
		    stmt.setString(2, employee.getFirstName());
		    stmt.setString(3, employee.getLastName());
		    stmt.setString(4, employee.getEmail());
		    stmt.setString(5, employee.getPhone());
		    stmt.setString(6, employee.getDepartment());
		    stmt.setDouble(7, employee.getSalary());

		    // Convert javat.util.Date to java.sql.Date
		    java.sql.Date sqlDate = new java.sql.Date(employee.getJoinDate().getTime());
		    stmt.setDate(8, sqlDate);
		    int rowsInserted = stmt.executeUpdate();
	        return rowsInserted > 0;		
		} catch (SQLException e) {	
			e.printStackTrace();
		    throw new EmployeeDataAccessException("Database error", e);
		} catch (NullPointerException e) {
			e.printStackTrace();
		    throw new EmployeeDataAccessException("Employee data is incomplete", e);
		}
	}

	public List<Employee> selectAllEmployees()  throws EmployeeDataAccessException 
	{
		List<Employee> employees =new ArrayList<>();
		
		try (Connection conn = DBUtil.getConnection();
			     PreparedStatement stmt = conn.prepareStatement(SQLConstants.SELECT_ALL_EMPLOYEES);
				ResultSet result=stmt.executeQuery();) 
		{
			while(result.next()) 
			{
				Employee emp =new Employee();
				emp.setEmployeeId(result.getInt(SQLConstants.EMP_ID));
				emp.setFirstName(result.getString(SQLConstants.FIRST_NAME));
				emp.setLastName(result.getString(SQLConstants.LAST_NAME));
				emp.setEmail(result.getString(SQLConstants.EMAIL));
				emp.setPhone(result.getString(SQLConstants.PHONE));
				emp.setDepartment(result.getString(SQLConstants.DEPARTMENT));
//				emp.setSalary(result.getDouble(SQLConstants.SALARY));
//				emp.setJoinDate(result.getDate(SQLConstants.JOIN_DATE));
				employees.add(emp);
			}	
		}catch (SQLException e) {
			e.printStackTrace();
	        throw new EmployeeDataAccessException("Error while fetching employee data", e);
	    } catch (NullPointerException e) {
	    	e.printStackTrace();
	        throw new EmployeeDataAccessException("Unexpected null value encountered", e);
	    }
		return employees;
	}
}
