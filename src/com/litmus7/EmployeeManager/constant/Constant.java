package com.litmus7.EmployeeManager.constant;

import java.util.HashMap;

public class Constant {
	public static final String insertToemployees="INSERT INTO employees (emp_id, first_name, last_name, email, phone, department, salary, join_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String select1WithEmployeeId="select 1 from employees where emp_id = ?";
	public static final String selectAllFromEmployee="select * From Employees";
    public static final HashMap<Integer, String>  errorCodeMap = new HashMap<>();
    static {     
        errorCodeMap.put(200, "Operation Successful");
        errorCodeMap.put(400, "Bad Request");
        errorCodeMap.put(404, "Not Found");
        errorCodeMap.put(500, "Internal Server Error");
        errorCodeMap.put(501, "Provided file is not a CSV");
        errorCodeMap.put(502, "File path is missing");
        errorCodeMap.put(505, "error writting file");
        errorCodeMap.put(206, "Partial success: some records failed to insert.");
        errorCodeMap.put(506, "Data write failed completely.");
        
    }
	
	
	
	
	
	

}
