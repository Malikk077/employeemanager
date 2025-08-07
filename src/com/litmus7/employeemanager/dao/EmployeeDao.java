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
import java.util.Arrays;
import java.util.List;



public class EmployeeDao 
{
	public boolean doesEmployeeExist(int id) throws EmployeeDataAccessException
	{
		
		try(Connection connection = DBUtil.getConnection();
	             PreparedStatement stmt = connection.prepareStatement(SQLConstants.SELECT_1_FROM_EMPLOYEES);) 
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
		
		try (Connection connection = DBUtil.getConnection();
		     PreparedStatement stmt = connection.prepareStatement(SQLConstants.INSERT_TO_EMPLOYEES)) 
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
		} 
	}

	public List<Employee> selectAllEmployees()  throws EmployeeDataAccessException 
	{
		List<Employee> employees =new ArrayList<>();
		
		try (Connection connection = DBUtil.getConnection();
			     PreparedStatement stmt = connection.prepareStatement(SQLConstants.SELECT_ALL_EMPLOYEES);
				ResultSet result=stmt.executeQuery();) 
		{
			while(result.next()) 
			{
				Employee employee =new Employee();
				employee.setEmployeeId(result.getInt(SQLConstants.EMP_ID));
				employee.setFirstName(result.getString(SQLConstants.FIRST_NAME));
				employee.setLastName(result.getString(SQLConstants.LAST_NAME));
				employee.setEmail(result.getString(SQLConstants.EMAIL));
				employee.setPhone(result.getString(SQLConstants.PHONE));
				employee.setDepartment(result.getString(SQLConstants.DEPARTMENT));
//				employee.setSalary(result.getDouble(SQLConstants.SALARY));
//				employee.setJoinDate(result.getDate(SQLConstants.JOIN_DATE));
				employees.add(employee);
			}	
		}catch (SQLException e) {
			e.printStackTrace();
	        throw new EmployeeDataAccessException("Error while fetching employee data", e);
		}
		return employees;
	}

	public Employee findById(int employeeId) throws EmployeeDataAccessException {
		
		try (Connection connection = DBUtil.getConnection();
			     PreparedStatement stmt = connection.prepareStatement(SQLConstants.SELECT_EMPLOYEE_BY_ID);) 
		{
			stmt.setInt(1, employeeId);
			try (ResultSet result=stmt.executeQuery();){
				while(result.next()) 
				{
					Employee employee =new Employee();
					employee.setEmployeeId(result.getInt(SQLConstants.EMP_ID));
					employee.setFirstName(result.getString(SQLConstants.FIRST_NAME));
					employee.setLastName(result.getString(SQLConstants.LAST_NAME));
					employee.setEmail(result.getString(SQLConstants.EMAIL));
					employee.setPhone(result.getString(SQLConstants.PHONE));
					employee.setDepartment(result.getString(SQLConstants.DEPARTMENT));
					employee.setSalary(result.getDouble(SQLConstants.SALARY));
					employee.setJoinDate(result.getDate(SQLConstants.JOIN_DATE));
					return employee;
				}	
			}
		}catch (SQLException e) {
			e.printStackTrace();
	        throw new EmployeeDataAccessException("Error while fetching employee data", e);
		}
		return null;
		}
	
	public int updateEmployee(Employee employee) throws EmployeeDataAccessException{
		
	   if(!doesEmployeeExist(employee.getEmployeeId()))
		   throw new EmployeeDataAccessException("Employee Does not Exist !!!");
	
		try (Connection connection = DBUtil.getConnection();
			     PreparedStatement stmt = connection.prepareStatement(SQLConstants.UPDATE_EMPLOYEE_BY_ID);) 
		{		
		    stmt.setString(1, employee.getFirstName());
		    stmt.setString(2, employee.getLastName());
		    stmt.setString(3, employee.getEmail());
		    stmt.setString(4, employee.getPhone());
		    stmt.setString(5, employee.getDepartment());
		    stmt.setDouble(6, employee.getSalary());
		    // Convert javat.util.Date to java.sql.Date
		    java.sql.Date sqlDate = new java.sql.Date(employee.getJoinDate().getTime());
		    stmt.setDate(7, sqlDate);
		    
		    //WHERE CLAUSE
		    stmt.setInt(8, employee.getEmployeeId());
		    
		    int rowsAffected = stmt.executeUpdate();
	        return rowsAffected ; 
		}catch (SQLException e) {
			e.printStackTrace();
	        throw new EmployeeDataAccessException("Error while updating employee data", e);
		}
	}
	
	public int deleteEmployee(int employeeId) throws EmployeeDataAccessException{
		
		   if(!doesEmployeeExist(employeeId))
			   throw new EmployeeDataAccessException("Employee Does not Exist !!!");
		
			try (Connection connection = DBUtil.getConnection();
				     PreparedStatement stmt = connection.prepareStatement(SQLConstants.DELETE_FROM_EMPLOYEES_WITH_ID);) 
			{		
			    stmt.setInt(1,employeeId );
			   
			    int rowsAffected = stmt.executeUpdate();
		        return rowsAffected ; 
			}catch (SQLException e) {
				e.printStackTrace();
		        throw new EmployeeDataAccessException("Error while Deleting employee data", e);
			}
		}
	
	public int[] batchInsertEmployees(List<Employee> employees) throws EmployeeDataAccessException{
		
		try (Connection connection = DBUtil.getConnection();
			     PreparedStatement stmt = connection.prepareStatement(SQLConstants.INSERT_TO_EMPLOYEES);) 
		{
			for(Employee employee:employees)
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
			    stmt.addBatch();				
			}
			int[] result = stmt.executeBatch();
			return result;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new EmployeeDataAccessException("Error while processing employee data", e);
			}
	   }
	
	
	public boolean transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) 
	        throws EmployeeDataAccessException {

	    boolean success = false;
	    Connection connection = null;

	    try {
	        connection = DBUtil.getConnection();
	        PreparedStatement stmt = connection.prepareStatement(SQLConstants.UPDATE_EMPLOYEE_DEPARTMENT_WITH_ID);

	        connection.setAutoCommit(false);

	        for (int employeeId : employeeIds) {
	            stmt.setString(1, newDepartment);
	            stmt.setInt(2, employeeId);
	            stmt.addBatch();
	        }

	        int[] result = stmt.executeBatch();
	        boolean hasZero = Arrays.stream(result).anyMatch(n -> n == 0);

	        if (hasZero) {
	            connection.rollback();
	        } else {
	            connection.commit();
	            success = true;
	        }

	        stmt.close(); 
	    } catch (SQLException e) {
	        throw new EmployeeDataAccessException("Error while updating employee data", e);
	    } finally {
	        if (connection != null) {
	            try {
	                if (!connection.getAutoCommit()) {
	                    connection.setAutoCommit(true);
	                }
	                connection.close(); 
	            } catch (SQLException e) {
	                throw new EmployeeDataAccessException("Error while resetting auto-commit", e);
	            }
	        }
	    }
	    return success;
	}




}

	
