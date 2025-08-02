package com.litmus7.EmployeeManager.dao;
import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.constant.SQLConstants;
import com.litmus7.EmployeeManager.util.DBUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;



public class EmployeeDao 
{
	public static boolean doesEmployeeExist(int id)
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
			 System.out.println("SQL error on duplicate check"+ e.getMessage());
		}
		return false;
	}
	
	public static boolean storeInDB(Employees employee) 
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

		    // Convert java.util.Date to java.sql.Date
		    java.sql.Date sqlDate = new java.sql.Date(employee.getJoinDate().getTime());
		    stmt.setDate(8, sqlDate);
		    int rowsInserted = stmt.executeUpdate();
	        return rowsInserted > 0;
			
			
			
		} 
		catch (SQLException e) 
		{
			 System.out.println("SQL error on duplicate check"+ e.getMessage());
			 return false;
		}
	}

	public static List<Employees> selectAllEmployees() 
	{
		List<Employees> employeeList =new ArrayList<>();
		
		try (Connection conn = DBUtil.getConnection();
			     PreparedStatement stmt = conn.prepareStatement(SQLConstants.SELECT_ALL_EMPLOYEES);
				ResultSet result=stmt.executeQuery();) 
		{
			while(result.next()) 
			{
				Employees emp =new Employees();
				emp.setEmployeeId(result.getInt("emp_id"));
				emp.setFirstName(result.getString("first_name"));
				emp.setLastName(result.getString("last_name"));
				emp.setEmail(result.getString("email"));
				emp.setPhone(result.getString("phone"));
				emp.setDepartment(result.getString("department"));
				emp.setSalary(result.getDouble("salary"));
				emp.setJoinDate(result.getDate("join_date"));
				employeeList.add(emp);
			}
				
		 }
		catch (SQLException e) 
		{
		    e.printStackTrace();
		}
		return employeeList;
	}
}
