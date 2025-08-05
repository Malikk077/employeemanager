package com.litmus7.EmployeeManager.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.litmus7.EmployeeManager.util.CsvUtil;
import com.litmus7.EmployeeManager.util.ValidationUtil;
import com.litums7.EmployeeManager.exception.EmployeeDataAccessException;
import com.litums7.EmployeeManager.exception.EmployeeServiceException;
import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.dao.EmployeeDao;

public class EmployeeService {
	
    EmployeeDao employeeDao = new EmployeeDao();
	public Map<String, Integer> writeToDb (String file)  
	{
		Map<String, Integer> count=new HashMap<>();    
		count.put("total", 0);
		count.put("success", 0);
		count.put("failure", 0);
		
		
		List<String[]> records = CsvUtil.readCSV(file);
        
		for (String[] values : records) 
        {
			count.put("total", count.get("total") + 1);
        	Employees employee =new Employees();
    		try
    		{
    			employee.setEmployeeId(Integer.parseInt(values[0].trim()));
    		    employee.setFirstName(values[1].trim());
    		    employee.setLastName(values[2].trim());
    		    employee.setEmail(values[3].trim());
    		    employee.setPhone(values[4].trim());
    		    employee.setDepartment(values[5].trim());
    		    employee.setSalary(Double.parseDouble(values[6].trim()));

    		    
    		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		    employee.setJoinDate(sdf.parse(values[7].trim()));		
    		 }
    		catch (NumberFormatException | ParseException | ArrayIndexOutOfBoundsException e) {
    		    System.err.println("Error in row " + count.get("total") + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
    		    count.put("failure", count.get("failure") + 1);
    		    continue;
    		}
    		if (!ValidationUtil.validateEmployee(employee)){
    			count.put("failure", count.get("failure") + 1);
    			continue;		
    		}
    		try {
    			if (employeeDao.doesEmployeeExist(employee.getEmployeeId())){
        			count.put("failure", count.get("failure") + 1);
        			continue;
    			}	 
    			if (employeeDao.saveEmployee(employee)) {
                    count.put("success", count.get("success") + 1);
                } else {
                    count.put("failure", count.get("failure") + 1);
                }	
    		}
    	    catch(EmployeeDataAccessException e) {
    	    	System.err.println("Database error while processing row " + count.get("total") + ": " + e.getMessage());
                count.put("failure", count.get("failure") + 1);
                // Do not throw here — continue to next record
            }    	
        }	
		return count;
	}
    			
    		
    		

	public List<Employees> readAllFromDb() throws EmployeeServiceException
	{
		
		try{
			return employeeDao.selectAllEmployees();
		}
		catch(EmployeeDataAccessException e) {
			System.err.println("Error: " + e.getMessage());
			throw new EmployeeServiceException(e);
		    
		   
		}
		

	}

}
