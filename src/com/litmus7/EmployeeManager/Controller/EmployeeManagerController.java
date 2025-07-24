package com.litmus7.EmployeeManager.Controller;
import com.litmus7.EmployeeManager.util.CsvUtil;

import java.util.ArrayList;
import java.util.List;

import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.Dto.Response;
import com.litmus7.EmployeeManager.service.EmployeeService;

public class EmployeeManagerController {
	EmployeeService employserv =new EmployeeService();
	

	public Response writeDataToDb(String file) 
	{
	    Response<Integer> obj = new Response(0,"",0);
	    try {
	    	if (file == null || file.trim().isEmpty()) {
		        return new Response<>(502, "File path is missing.", null);
		    }

		    if (!file.toLowerCase().endsWith(".csv")) {
		        return new Response<>(501, "Provided file is not a CSV.", null);
		    }
	        List<String[]> records = CsvUtil.readCSV(file);
	        boolean isWritten=false;
	        for (String[] values : records) 
	        {
	            isWritten= employserv.writeToDb(values);
	            if (isWritten)
	                 obj.setData(obj.getData()+1);
	        }

	        if(isWritten) 
	        {
	        	obj.setErrorMessage("Data written successfully");
		        obj.setStatusCode(200);
	        }
	        else
	        {
	        	obj.setErrorMessage("Unexpected error: " );
		        obj.setStatusCode(500);
	        }
	        	
	            
	    } catch (Exception e) {
	        obj.setErrorMessage("Unexpected error: " + e.getMessage());
	        obj.setStatusCode(500);
	    }

	    return obj;
    }


	public Response<List<Employees>> getAllEmployees()
	{
		List<Employees> employeeList=new ArrayList<>();
		employeeList=employserv.readAllFromDb();
		if (employeeList == null) {
	        return new Response<>(500, "Failed to fetch data", null);
	    }
		Response<List<Employees>> obj = new Response<>(200,"data Fetched Succesfully",employeeList);
		return obj;
	}
}