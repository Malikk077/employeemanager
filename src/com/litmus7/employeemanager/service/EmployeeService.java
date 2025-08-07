package com.litmus7.employeemanager.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.litmus7.employeemanager.util.CsvUtil;
import com.litmus7.employeemanager.util.ValidationUtil;
import com.litmus7.employeemanager.exception.EmployeeDataAccessException;
import com.litmus7.employeemanager.exception.EmployeeServiceException;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dao.EmployeeDao;

public class EmployeeService {
    private static final Logger logger = LogManager.getLogger(EmployeeService.class);

	
    EmployeeDao employeeDao = new EmployeeDao();
	public Map<String, Integer> writeToDb (String file) throws EmployeeServiceException  
	{
		logger.info("writeToDb started for file: {}", file);
		List<String[]> records=new ArrayList<>();
		Map<String, Integer> count=new HashMap<>();    
		count.put("total", 0);
		count.put("success", 0);
		count.put("failure", 0);
		try {
			 records = CsvUtil.readCSV(file);
		}catch(EmployeeDataAccessException e){
			logger.error("Failed to read CSV file: {}", file, e);
			throw new EmployeeServiceException("Failed to write Csv File",e);
		} 
		for (String[] values : records) 
        {
			count.put("total", count.get("total") + 1);
        	Employee employee =new Employee();
        	if (values.length < 8) {
        		logger.warn("Skipping row {}: Incomplete data", count.get("total"));
    		    count.put("failure", count.get("failure") + 1);
    		    continue;
    		}
    		try{
    			employee.setEmployeeId(Integer.parseInt(values[0].trim()));
    		    employee.setFirstName(values[1].trim());
    		    employee.setLastName(values[2].trim());
    		    employee.setEmail(values[3].trim());
    		    employee.setPhone(values[4].trim());
    		    employee.setDepartment(values[5].trim());
    		    employee.setSalary(Double.parseDouble(values[6].trim()));

    		    
    		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		    employee.setJoinDate(sdf.parse(values[7].trim()));		
    	   
    		
    		    if (!ValidationUtil.validateEmployee(employee)){
    		       logger.warn("Validation failed for employee ID: {}", employee.getEmployeeId());
 			       count.put("failure", count.get("failure") + 1);
 			      continue;    	    	
    		    }
    		    if (employeeDao.doesEmployeeExist(employee.getEmployeeId())){
    				logger.warn("Duplicate employee ID found: {}", employee.getEmployeeId());
    				count.put("failure", count.get("failure") + 1);
        			continue;
    		    }else {
                	logger.error("Failed to save employee: {}", employee.getEmployeeId());
                    count.put("failure", count.get("failure") + 1);
                }
    		}catch (NumberFormatException | ParseException | ArrayIndexOutOfBoundsException e) {
    			logger.error("Parsing error in row {}: {}", count.get("total"), e.getMessage());
    		    count.put("failure", count.get("failure") + 1);
    		    continue;
    		}catch(EmployeeDataAccessException e) {
    			logger.fatal("Database error while saving employee", e);
    			 throw new EmployeeServiceException("Fatal DB error: Connection lost", e);
            }
        }
		logger.info("writeToDb completed. Total: {}, Success: {}, Failure: {}", count.get("total"), count.get("success"), count.get("failure"));
		return count;
	}

	public List<Employee> readAllFromDb() throws EmployeeServiceException
	{
		
		logger.info("[readAllFromDb] Fetching all employees from the database");
		try{
			List<Employee> employees = employeeDao.selectAllEmployees();
	        logger.info("[readAllFromDb] Retrieved {} employees", employees.size());
	        return employees;
		}
		catch(EmployeeDataAccessException e) {
			logger.error("[readAllFromDb] Error while fetching employees", e);
			throw new EmployeeServiceException(e);	     
		}
	}
	
	public Employee getEmployeeById(int employeeId) throws EmployeeServiceException
	{
		logger.info("[getEmployeeById] Attempting to fetch employee with ID: {}", employeeId);
		try{
			 Employee employee = employeeDao.findById(employeeId);

		        if (employee != null) {
		            logger.info("[getEmployeeById] Employee found.");
		        } else {
		            logger.warn("[getEmployeeById] No employee found with ID: {}", employeeId);
		        }

		        return employee;
		}
		catch(EmployeeDataAccessException e) {
			logger.error("[getEmployeeById] Error while fetching employee with ID: {}", employeeId, e);
			throw new EmployeeServiceException(e);	     
		}
	}
	
	public int updateEmployee(Employee employee) throws EmployeeServiceException{
		logger.info("[updateEmployee] Attempting to update employee: {}", employee);
		try {	
			int rowsAffected = employeeDao.updateEmployee(employee);
	        if (rowsAffected > 0) {
	            logger.info("[updateEmployee] Successfully updated employee with ID: {}", employee.getEmployeeId());
	        } else {
	            logger.warn("[updateEmployee] No records updated for employee with ID: {}", employee.getEmployeeId());
	        }
	        return rowsAffected;
		}catch(EmployeeDataAccessException e) {
			logger.error("[updateEmployee] Error while updating employee with ID: {}", employee.getEmployeeId(), e);
			throw new EmployeeServiceException(e);	     
		}
	}
	
	public int deleteEmployeeById(int employeeId) throws EmployeeServiceException{
		logger.info("[deleteEmployeeById] Attempting to delete employee with ID: {}", employeeId);
		try {
			Employee employee = employeeDao.findById(employeeId);
	        if (employee != null) {
	            logger.info("[deleteEmployeeById] Found and deleted employee with ID: {}", employeeId);
	        } else {
	            logger.warn("[deleteEmployeeById] No employee found with ID: {}", employeeId);
	        }

	        return employeeDao.deleteEmployee(employeeId);
		}catch(EmployeeDataAccessException e) {
			logger.error("[deleteEmployeeById] Error while deleting employee with ID: {}", employeeId, e);
			throw new EmployeeServiceException(e);	     
		}
	}
	
	
	public boolean addEmployee(Employee employee) throws EmployeeServiceException{
		logger.info("[addEmployee] Attempting to add employee with ID: {}", employee.getEmployeeId());
		try {
			if(employeeDao.doesEmployeeExist(employee.getEmployeeId())){
	            logger.warn("[addEmployee] Employee with ID {} already exists.", employee.getEmployeeId());
	            throw new EmployeeServiceException("Employee Already Exists !!!");
	        }
			boolean result =  employeeDao.saveEmployee(employee);
			if (result) {
				logger.info("[addEmployee] Employee added Succesfully ,ID : {}",employee.getEmployeeId());
			}else {
				logger.warn("[addEmployee] Failed to add employee with ID: {}", employee.getEmployeeId());

			}
			return result;
		}catch(EmployeeDataAccessException e) {
			 logger.error("[addEmployee] Error while adding employee with ID: {}", employee.getEmployeeId(), e);
			throw new EmployeeServiceException(e);	     
		}
	}
	
	public int[] batchInsertEmployees(List<Employee> employees) throws EmployeeServiceException{
		
		logger.info("[batchInsertEmployees] Received {} employees for batch insert.", employees.size());
		try {
			List<Employee> refinedEmployeeList=new ArrayList<>();
			for(Employee employee:employees) {
				if(!employeeDao.doesEmployeeExist(employee.getEmployeeId()) && ValidationUtil.validateEmployee(employee) && (!refinedEmployeeList.contains(employee))) {
					refinedEmployeeList.add(employee);
				}
			}	
			logger.info("[batchInsertEmployees] Refined list size after validation: {}", refinedEmployeeList.size());
	        int[] result = employeeDao.batchInsertEmployees(refinedEmployeeList);
	        logger.info("[batchInsertEmployees] Batch insert completed. Records inserted: {}", result.length);

	        return result;
		}catch(EmployeeDataAccessException e){
			logger.error("[batchInsertEmployees] Error during batch insert", e);
			throw new EmployeeServiceException(e);
		}
	}
	
	public boolean transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment)throws EmployeeServiceException{
		logger.info("[transferEmployeesToDepartment] Initiating transfer of {} employees to department '{}'", employeeIds.size(), newDepartment);
		try {
			for (int employeeId:employeeIds) {
				if (!employeeDao.doesEmployeeExist(employeeId)) {
	                logger.warn("[transferEmployeesToDepartment] Employee with ID {} does not exist", employeeId);
	                throw new EmployeeServiceException("One or more employee IDs do not exist");
	            }
			}
			boolean success = employeeDao.transferEmployeesToDepartment(employeeIds, newDepartment);
			if (success) {
	            logger.info("[transferEmployeesToDepartment] Successfully transferred all employees to '{}'", newDepartment);
	        } else {
	            logger.warn("[transferEmployeesToDepartment] Transfer failed. Some records may not have been updated.");
	        }
	        return success;	
		}catch(EmployeeDataAccessException e) {
			logger.error("[transferEmployeesToDepartment] Error occurred during department transfer", e);
			throw new EmployeeServiceException("Error During Department Transfers",e);		
		}
	}
	
	
	
	
	

	
	
	
	
}
