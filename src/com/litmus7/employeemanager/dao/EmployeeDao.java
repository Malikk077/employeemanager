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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;







public class EmployeeDao 
{
	private static final Logger logger = LogManager.getLogger(EmployeeDao.class);
	
	public boolean doesEmployeeExist(int id) throws EmployeeDataAccessException
	{
		logger.trace("Entering doesEmployeeExist with id = {}  ",id);
		try(Connection connection = DBUtil.getConnection();
	             PreparedStatement stmt = connection.prepareStatement(SQLConstants.SELECT_1_FROM_EMPLOYEES);) 
		{
			stmt.setInt(1, id);
            logger.debug("Executing SQL: {} with id = {}", SQLConstants.SELECT_1_FROM_EMPLOYEES, id);

			try (ResultSet result = stmt.executeQuery()) {
				boolean exists = result.next();
                logger.info("Employee existence check for ID {}: {}", id, exists);
                logger.trace("Exiting doesEmployeeExist()");
                return exists;
	        }
		}
		 catch (SQLException e) 
		{
	         logger.error("SQL Exception while checking if employee exists. ID: {}", id, e);
			 throw new EmployeeDataAccessException("SQL error on duplicate check", e);
		}
	}
	
	public boolean saveEmployee(Employee employee)  throws EmployeeDataAccessException {
		
		logger.trace("Entering saveEmployee((Employee employee)");
		logger.info("Attempting to save employee with ID: {}", employee.getEmployeeId());
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
		    logger.debug("Executing SQL: {} with employee data: {}, {}, {}, {}, {}, {}, {}",SQLConstants.INSERT_TO_EMPLOYEES,
	                employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(),
	                employee.getEmail(), employee.getPhone(), employee.getDepartment(), employee.getSalary());
		    
		    
		    int rowsInserted = stmt.executeUpdate();
		    
		    if (rowsInserted > 0) {
	            logger.info("Employee inserted successfully: ID {}", employee.getEmployeeId());
	        } else {
	            logger.warn("No rows inserted for employee: ID {}", employee.getEmployeeId());
	        }

	        logger.trace("Exiting saveEmployee()");
	        return rowsInserted > 0;		
		} catch (SQLException e) {	
			logger.error("Error while saving employee with ID {}:{} ",employee.getEmployeeId(),e.getMessage(),e);
			new EmployeeDataAccessException("Database error while saving employee", e);
		}
		return false;
		
	}

	public List<Employee> selectAllEmployees()  throws EmployeeDataAccessException 
	{
		logger.trace("Entering selectAllEmployees()");
		logger.info("Attempting to selectAllEmployees ");
		
		List<Employee> employees =new ArrayList<>();
		
		try (Connection connection = DBUtil.getConnection();
			     PreparedStatement stmt = connection.prepareStatement(SQLConstants.SELECT_ALL_EMPLOYEES);
				ResultSet result=stmt.executeQuery();) 
		{
			while(result.next()) 
			{
				logger.info("Attempting to add employee with id : {} to the list ",result.getInt(SQLConstants.EMP_ID));
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
			logger.error("Error while selecting Employee : {} : {}  ",e.getMessage(),e);
	        throw new EmployeeDataAccessException("Error while fetching employee data", e);
		}
		logger.info("Successfully fetched {} employee(s)", employees.size());
        logger.trace("Exiting selectAllEmployees()");
        return employees;
	}

	public Employee findById(int employeeId) throws EmployeeDataAccessException {
		
		logger.trace("Entering findById() with employeeId = {}", employeeId);
		try (Connection connection = DBUtil.getConnection();
			     PreparedStatement stmt = connection.prepareStatement(SQLConstants.SELECT_EMPLOYEE_BY_ID);) 
		{
			stmt.setInt(1, employeeId);
			logger.debug("Executing query to fetch employee with ID = {}", employeeId);
			try (ResultSet result=stmt.executeQuery();){
				if(result.next()) 
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
					logger.info("Employee found with ID = {}", employeeId);
	                logger.trace("Exiting findById()");
					return employee;
				}else {
	                logger.warn("No employee found with ID = {}", employeeId);
	            }	
			}
		}catch (SQLException e) {
			logger.error("SQL error while fetching employee by ID = {}: {}", employeeId, e.getMessage(), e);;
	        throw new EmployeeDataAccessException("Error while fetching employee data", e);
		}
		logger.trace("Exiting findById() with null result for employeeId = {}", employeeId);
		return null;
		}
	
	public int updateEmployee(Employee employee) throws EmployeeDataAccessException{
		
	   logger.trace("Entering updateEmployee(Employee employee)");
	   logger.info("Attempting to update employee with ID: {}", employee.getEmployeeId());
	   if(!doesEmployeeExist(employee.getEmployeeId())) {
		   logger.warn("Employee with ID {} does not exist", employee.getEmployeeId());
		   throw new EmployeeDataAccessException("Employee Does not Exist !!!");
		   
	   }
		   
		   
	
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
		    logger.debug("Executing SQL: {} with data: id={}, fname={}, lname={}, email={}, phone={}, dept={}, salary={}, date={}",SQLConstants.UPDATE_EMPLOYEE_BY_ID,
	                employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(),
	                employee.getEmail(), employee.getPhone(), employee.getDepartment(), employee.getSalary(), sqlDate);
		    int rowsAffected = stmt.executeUpdate();
		    logger.trace("Exiting updateEmployee()");
	        return rowsAffected ; 
		}catch (SQLException e) {
			logger.error("Error while updating employee with ID {}: {}", employee.getEmployeeId(), e.getMessage(), e);
	        throw new EmployeeDataAccessException("Error while updating employee data", e);
		}
	}
	
	public int deleteEmployee(int employeeId) throws EmployeeDataAccessException{
		
		   logger.trace("Entering deleteEmployee(int employeeId)");
		   logger.info("Attempting to delete employee with ID: {}", employeeId);
		   if(!doesEmployeeExist(employeeId)) {
			   logger.warn("Employee with ID {} does not exist", employeeId);
			   throw new EmployeeDataAccessException("Employee Does not Exist !!!"); 
		   }
			try (Connection connection = DBUtil.getConnection();
				     PreparedStatement stmt = connection.prepareStatement(SQLConstants.DELETE_FROM_EMPLOYEES_WITH_ID);) 
			{		
			    stmt.setInt(1,employeeId );
			    
			    logger.debug("Executing SQL: {} with employeeId={}", SQLConstants.DELETE_FROM_EMPLOYEES_WITH_ID, employeeId);
			    
			    int rowsAffected = stmt.executeUpdate();
			    if (rowsAffected > 0) {
		            logger.info("Successfully deleted employee with ID {}", employeeId);
		        } else {
		            logger.warn("No rows deleted for employee with ID {}", employeeId);
		        }
			    logger.trace("Exiting deleteEmployee()");
		        return rowsAffected ; 
			}catch (SQLException e) {
				logger.error("Error while deleting employee with ID {}: {}", employeeId, e.getMessage(), e);
		        throw new EmployeeDataAccessException("Error while Deleting employee data", e);
			}
		}
	
	public int[] batchInsertEmployees(List<Employee> employees) throws EmployeeDataAccessException{
		
		logger.trace("Entering batchInsertEmployees(List<Employee>)");
	    logger.info("Attempting batch insert for {} employees", employees.size());
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
			    logger.debug("Adding to batch: Employee ID {}, Name: {} {}", 
                        employee.getEmployeeId(), employee.getFirstName(), employee.getLastName());
			    stmt.addBatch();				
			}
			int[] result = stmt.executeBatch();
			logger.info("Batch insert completed. Total rows affected: {}", Arrays.stream(result).sum());
	        logger.trace("Exiting batchInsertEmployees()");
			return result;
			
		}catch(SQLException e) {
			logger.error("Batch insert failed: {}", e.getMessage(), e);
			throw new EmployeeDataAccessException("Error while processing employee data", e);
			}
	   }
	
	
	public boolean transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) 
	        throws EmployeeDataAccessException {

		logger.trace("Entering transferEmployeesToDepartment(List<Integer>, String)");
	    logger.info("Attempting to transfer {} employees to department '{}'", employeeIds.size(), newDepartment);
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
	            logger.debug("Prepared batch update for employee ID {} to department '{}'", employeeId, newDepartment);
	        }

	        int[] result = stmt.executeBatch();
	        boolean hasZero = Arrays.stream(result).anyMatch(n -> n == 0);

	        if (hasZero) {
	        	logger.warn("One or more employee IDs not found. Performing rollback.");
	            connection.rollback();
	        } else {
	            connection.commit();
	            success = true;
	            logger.info("All employees transferred successfully to department '{}'", newDepartment);
	        }
	        stmt.close(); 
	    } catch (SQLException e) {
	    	logger.error("Error during employee transfer to department '{}': {}", newDepartment, e.getMessage(), e);
	        throw new EmployeeDataAccessException("Error while updating employee data", e);
	    } finally {
	        if (connection != null) {
	            try {
	                if (!connection.getAutoCommit()) {
	                    connection.setAutoCommit(true);
	                }
	                connection.close();
	                logger.debug("Database connection closed and auto-commit reset");
	            } catch (SQLException e) {
	            	logger.error("Error while resetting auto-commit or closing connection: {}", e.getMessage(), e);
	                throw new EmployeeDataAccessException("Error while resetting auto-commit", e);
	            }
	        }
	    }
	    logger.trace("Exiting transferEmployeesToDepartment() with result: {}", success);
	    return success;
	}




}

	
