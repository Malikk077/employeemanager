package com.litmus7.EmployeeManager.Controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.Dto.Response;
import com.litmus7.EmployeeManager.service.EmployeeService;
import com.litums7.EmployeeManager.exception.EmployeeServiceException;

public class EmployeeManagerController {
	EmployeeService employeeService =new EmployeeService();
	

	public Response<Integer> writeDataToDb(String file)
	{
		Map<String, Integer> output = new HashMap<>();
	    try {
	    	if (file == null || file.trim().isEmpty()) {
		        return new Response<>(502, "File path is missing.");
		    }if (!file.toLowerCase().endsWith(".csv")) {
		        return new Response<>(501, "Provided file is not a CSV.");
		    }
		    try{
	            output = employeeService.writeToDb(file);
	        }catch(EmployeeServiceException e){
	        	e.printStackTrace();
	        	return new Response<>(500, " error occurred  : "+e.getMessage());
	        }

	        int successCount = output.getOrDefault("success", 0);
//	        int failureCount = output.getOrDefault("failure", 0);
	        int totalCount   = output.getOrDefault("total", 0);

	        
	        if (successCount == 0) {
		        return new Response<>(500, "No records were inserted.");
		    } else if (successCount < totalCount) {
		        return new Response<>(207, "Partial insert: " + successCount + " of " + totalCount + " inserted.", successCount);
		    } else
		        return new Response<>(200, null, successCount); 
	    }catch (Exception e) {
	        e.printStackTrace(); // log full stack trace
	        return new Response<>(500, "Unexpected error occurred. "+e.getMessage());
	    }
	    
	}
	public Response<List<Employees>> getAllEmployees()
	{
		List<Employees> employees=new ArrayList<>();
		try{

			employees=employeeService.readAllFromDb();	
			return new Response<>(200,"data Fetched Succesfully",employees);
			
		}catch (EmployeeServiceException e) {
	        return new Response<>(500, "Exception while fetching data: " + e.getMessage());
	    }
	}
		
}
	
