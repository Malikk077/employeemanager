package com.litmus7.employeemanager.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.service.EmployeeService;
import com.litmus7.employeemanager.exception.EmployeeServiceException;
import com.litmus7.employeemanager.constant.Constant;


public class EmployeeManagerController {
	EmployeeService employeeService =new EmployeeService();
	

	public Response<Integer> writeDataToDb(String file)
	{
		Map<String, Integer> output = new HashMap<>();
	    try {
	    	if (file == null || file.trim().isEmpty()) {
		        return new Response<>(Constant.FILE_PATH_MISSING , "File path is missing.");
		    }if (!file.toLowerCase().endsWith(".csv")) {
		        return new Response<>(Constant.FILE_NOT_CSV, "Provided file is not a CSV.");
		    }
		    try{
	            output = employeeService.writeToDb(file);
	        }catch(EmployeeServiceException e){
	        	e.printStackTrace();
	        	return new Response<>(Constant.FAILURE, " error occurred  : "+e.getMessage());
	        }

	        int successCount = output.getOrDefault("success", 0);
//	        int failureCount = output.getOrDefault("failure", 0);
	        int totalCount   = output.getOrDefault("total", 0);

	        
	        if (successCount == 0) {
		        return new Response<>(Constant.FAILURE, "No records were inserted.");
		    } else if (successCount < totalCount) {
		        return new Response<>(Constant.PARTIAL_SUCCESS, "Partial insert: " + successCount + " of " + totalCount + " inserted.", successCount);
		    } else
		        return new Response<>(Constant.SUCCESS, null, successCount); 
	    }catch (Exception e) {
	        e.printStackTrace(); // log full stack trace
	        return new Response<>(Constant.FAILURE, "Unexpected error occurred. "+e.getMessage());
	    }
	    
	}
	public Response<List<Employee>> getAllEmployees()
	{
		try{

			List<Employee> employees=employeeService.readAllFromDb();	
			return new Response<>(Constant.SUCCESS,"data Fetched Succesfully",employees);
			
		}catch (EmployeeServiceException e) {
	        return new Response<>(Constant.FAILURE, "Exception while fetching data: " + e.getMessage());
	    }
	}
	
	public Response<Employee> getEmployeeById(int employeeId) {
		try {
			Employee employee = employeeService.getEmployeeById(employeeId);
			return new Response<>(Constant.SUCCESS,"Record fetched Succesfully: ",employee);	
		}catch(EmployeeServiceException e) {
			return new Response<>(Constant.FAILURE, "Exception while fetching data: " + e.getMessage());
		}
		
	}
}
	
