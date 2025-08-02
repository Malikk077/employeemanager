package com.litmus7.EmployeeManager.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.Dto.Response;
import com.litmus7.EmployeeManager.service.EmployeeService;

public class EmployeeManagerController {
	EmployeeService employeeService =new EmployeeService();
	

	public Response<Integer> writeDataToDb(String file) 
	{
	    try {
	    	if (file == null || file.trim().isEmpty()) {
		        return new Response<>(502, "File path is missing.", null);
		    }

		    if (!file.toLowerCase().endsWith(".csv")) {
		        return new Response<>(501, "Provided file is not a CSV.", null);
		    }
		    
	        Map<String, Integer> output = employeeService.writeToDb(file);

	        int successCount = output.getOrDefault("success", 0);
//	        int failureCount = output.getOrDefault("failure", 0);
	        int totalCount   = output.getOrDefault("total", 0);

	        
	        if (successCount == 0) {
		        return new Response<>(500, "No records were inserted.", null);
		    } else if (successCount < totalCount) {
		        return new Response<>(207, "Partial insert: " + successCount + " of " + totalCount + " inserted.", successCount);
		    } else
		        return new Response<>(200, null, successCount); 
		    }

	    catch (Exception e) {
	        e.printStackTrace();
	        return new Response<>(500, "An error occurred while writing data to DB.", null);
	        }
	}
	public Response<List<Employees>> getAllEmployees()
	{
		List<Employees> employeeList=new ArrayList<>();
		employeeList=employeeService.readAllFromDb();
		if (employeeList == null || employeeList.isEmpty()) {
	        return new Response<>(500, "Failed to fetch data", null);
	    }
		return new Response<>(200,"data Fetched Succesfully",employeeList);
	}
}
