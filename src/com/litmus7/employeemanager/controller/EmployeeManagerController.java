package com.litmus7.employeemanager.controller;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.service.EmployeeService;
import com.litmus7.employeemanager.exception.EmployeeServiceException;
import com.litmus7.employeemanager.constant.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmployeeManagerController {
	
	private static final Logger logger = LogManager.getLogger(EmployeeManagerController.class);
	EmployeeService employeeService =new EmployeeService();
	

	public Response<Integer> writeDataToDb(String file)
	{
		logger.info("[writeDataToDb] Received file path: {}", file);
	    try {
	    	if (file == null || file.trim().isEmpty()) {
	    		logger.warn("[writeDataToDb] File path is missing.");
		        return new Response<>(Constant.FILE_PATH_MISSING , "File path is missing.");
		    }if (!file.toLowerCase().endsWith(".csv")) {
		    	logger.warn("[writeDataToDb] File provided is not a CSV: {}", file);
		        return new Response<>(Constant.FILE_NOT_CSV, "Provided file is not a CSV.");
		    }
		    
		    Map<String, Integer> output = employeeService.writeToDb(file);
	  
		    int successCount = output.getOrDefault("success", 0);
//	        int failureCount = output.getOrDefault("failure", 0);
	        int totalCount   = output.getOrDefault("total", 0);

		    logger.info("[writeDataToDb] File processed. Total: {}, Success: {}", totalCount, successCount);
	        
	        if (successCount == 0) {
	        	logger.warn("[writeDataToDb] No records were inserted from file: {}", file);
		        return new Response<>(Constant.FAILURE, "No records were inserted.");
		    } else if (successCount < totalCount) {
		    	logger.warn("[writeDataToDb] Partial insert: {} of {} records inserted.", successCount, totalCount);
		        return new Response<>(Constant.PARTIAL_SUCCESS, "Partial insert: " + successCount + " of " + totalCount + " inserted.", successCount);
		    } else
		    	logger.info("[writeDataToDb] All records inserted successfully.");
		        return new Response<>(Constant.SUCCESS, null, successCount); 
	    }catch(EmployeeServiceException e){
	    	  logger.error("[writeDataToDb] Service error occurred: {}", e.getMessage(), e);
        	return new Response<>(Constant.FAILURE, " error occurred  : "+e.getMessage());
        }catch (Exception e) {
        	logger.error("[writeDataToDb] Unexpected error occurred: {}", e.getMessage(), e);
	        return new Response<>(Constant.FAILURE, "Unexpected error occurred. "+e.getMessage());
	    }
	    
	}
	public Response<List<Employee>> getAllEmployees()
	{
		logger.info("[getAllEmployees] Request received to fetch all employees");
		try{

			List<Employee> employees=employeeService.readAllFromDb();
			if(employees==null || employees.isEmpty()) {
				logger.warn("[getAllEmployees] No employee records found in the database.");
				return new Response<>(Constant.NO_DATA_FOUND,"No Records Found !!! ");
			}else {
				logger.info("[getAllEmployees] Successfully fetched {} employee(s)", employees.size());
				return new Response<>(Constant.SUCCESS,"data Fetched Succesfully",employees);	
			}
			
		}catch (EmployeeServiceException e) {
			logger.error("[getAllEmployees] Exception occurred while fetching employees: {}", e.getMessage(), e);
	        return new Response<>(Constant.FAILURE, "Exception while fetching data: " + e.getMessage());
	    }
	}
	
	public Response<Employee> getEmployeeById(int employeeId) {
		
		logger.info("[getEmployeeById] Request received for employeeId: {}", employeeId);
		try {
			Employee employee = employeeService.getEmployeeById(employeeId);
			if(employee==null) {
	            logger.warn("[getEmployeeById] No employee found for ID: {}", employeeId);
	            return new Response<>(Constant.NO_DATA_FOUND, "Employee Doesn't Exist !!!");
	        } else {
	            logger.info("[getEmployeeById] Successfully fetched employee for ID: {}", employeeId);
	            return new Response<>(Constant.SUCCESS, "Record fetched successfully", employee);
	        }	
		}catch(EmployeeServiceException e) {
			logger.error("[getEmployeeById] Exception while fetching employee ID {}: {}", employeeId, e.getMessage(), e);
			return new Response<>(Constant.FAILURE, "Exception while fetching data: " + e.getMessage());
		}		
	}
	
	
	public Response<Integer>  updateEmployee(Employee employee)
	{
		logger.info("[updateEmployee] Update request received for Employee ID: {}", employee.getEmployeeId());
		try{
			int result=employeeService.updateEmployee(employee);
			if (result>0) {
				logger.info("[updateEmployee] Successfully updated Employee ID: {}, Rows affected: {}", employee.getEmployeeId(), result);
				return new Response<>(Constant.SUCCESS,"Record updated Succesfully, Rows Affected: ",result);
			}else {
				logger.warn("[updateEmployee] No rows updated for Employee ID: {}", employee.getEmployeeId());
				return new Response<>(Constant.FAILURE, "Record update failed, Rows Affected: " ,result);		
			}
		}catch(EmployeeServiceException e) {
			logger.error("[updateEmployee] Exception while updating Employee ID: {}: {}", employee.getEmployeeId(), e.getMessage(), e);
			return new Response<>(Constant.FAILURE, "Exception while fetching data: " + e.getMessage());
		}
	}
	
	public Response<Integer>  deleteEmployeeById(int employeeId)
	{
		logger.info("[deleteEmployeeById] Delete request received for Employee ID: {}", employeeId);
		try{
			int result=employeeService.deleteEmployeeById(employeeId);
			if (result>0){
	            logger.info("[deleteEmployeeById] Successfully deleted Employee ID: {}, Rows affected: {}", employeeId, result);
	            return new Response<>(Constant.SUCCESS, "Record deleted successfully. Rows affected: ", result);
	        } else {
	            logger.warn("[deleteEmployeeById] No record deleted for Employee ID: {}", employeeId);
	            return new Response<>(Constant.FAILURE, "Record deletion failed. Rows affected: ", 0);
	        }
		}catch(EmployeeServiceException e) {
			logger.error("[deleteEmployeeById] Exception while deleting Employee ID: {}: {}", employeeId, e.getMessage(), e);
			return new Response<>(Constant.FAILURE, "Exception while Deleting data: " + e.getMessage());
		}
	}
	
	public Response<Integer> addEmployee(Employee employee)
	{
		logger.info("[addEmployee] Add request received for Employee ID: {}", employee.getEmployeeId());
		try {
			if (employeeService.addEmployee(employee)) {
	            logger.info("[addEmployee] Employee inserted successfully. Employee ID: {}", employee.getEmployeeId());
	            return new Response<>(Constant.SUCCESS, "Record inserted successfully. Row ID: ", employee.getEmployeeId());
	        } else {
	            logger.warn("[addEmployee] Employee insertion failed for Employee ID: {}", employee.getEmployeeId());
	            return new Response<>(Constant.FAILURE, "Record insertion failed. Rows affected: ", 0);
	        }		
		}catch(EmployeeServiceException e) {
			logger.error("[addEmployee] Exception while inserting employee with ID: {}: {}", employee.getEmployeeId(), e.getMessage(), e);
			return new Response<>(Constant.FAILURE, "Exception while Inserting data: " + e.getMessage());
		}
	}
	
	
	public Response<Integer> batchInsertEmployees(List<Employee> employees){
		
		logger.info("[batchInsertEmployees] Batch insert request received. Total input records: {}", employees.size());
		try {
			int[] result = employeeService.batchInsertEmployees(employees);
			int totalUpdated = Arrays.stream(result).sum();
			if (totalUpdated == employees.size()) {
	            logger.info("[batchInsertEmployees] All records inserted successfully. Rows inserted: {}", totalUpdated);
	            return new Response<>(Constant.SUCCESS, "All Records Inserted Successfully, Rows Inserted: ", totalUpdated);
	        } else if (totalUpdated > 0) {
	            logger.warn("[batchInsertEmployees] Partial insert. Expected: {}, Inserted: {}", employees.size(), totalUpdated);
	            return new Response<>(Constant.PARTIAL_SUCCESS, "Records Inserted Partially, Rows Inserted: ", totalUpdated);
	        } else {
	            logger.warn("[batchInsertEmployees] Batch insertion failed. No rows inserted.");
	            return new Response<>(Constant.FAILURE, "Records Insertion Failed!!! Rows Affected: 0");
	        }
		}catch(EmployeeServiceException e) {
			logger.error("[batchInsertEmployees] Exception during batch insert: {}", e.getMessage(), e);
			return new Response<>(Constant.FAILURE, "Exception while batch Insertion : " + e.getMessage());
		}
	}
	public Response<Integer> transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment){
		
		logger.info("[transferEmployeesToDepartment] Transfer request received for employee IDs: {} to department: {}", employeeIds, newDepartment);
		try {
			if ( employeeService.transferEmployeesToDepartment( employeeIds, newDepartment)) {
				logger.info("[transferEmployeesToDepartment] All records updated successfully.");
				return new Response<>(Constant.SUCCESS,"All Recocrds Updated Successfully");
			}else {
				logger.warn("[transferEmployeesToDepartment] No records were updated.");
				return new Response<>(Constant.FAILURE,"No changes have been done");
			}
		}catch(EmployeeServiceException e) {
			logger.error("[transferEmployeesToDepartment] Exception during department transfer: {}", e.getMessage(), e);
			return new Response<>(Constant.FAILURE, "Exception while Updating , All Changes Reverted : " + e.getMessage());	
		}
		
	}
	
	
		
	
}
	
