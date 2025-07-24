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
	        List<String[]> records = CsvUtil.readCSV(file);
	        for (String[] values : records) 
	        {
	            employserv.writeToDb(values);
	            obj.setData(obj.getData()+1);
	        }

	        
	        obj.setErrorMessage("Data written successfully");
	        obj.setStatusCode(200);
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