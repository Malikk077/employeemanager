package com.litmus7.employeemanager.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.litmus7.employeemanager.util.CsvUtil;
import com.litmus7.employeemanager.util.ValidationUtil;
import com.litmus7.employeemanager.exception.EmployeeDataAccessException;
import com.litmus7.employeemanager.exception.EmployeeServiceException;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dao.EmployeeDao;

public class EmployeeService {
	
    EmployeeDao employeeDao = new EmployeeDao();
	public Map<String, Integer> writeToDb (String file) throws EmployeeServiceException  
	{
		List<String[]> records=new ArrayList<>();
		Map<String, Integer> count=new HashMap<>();    
		count.put("total", 0);
		count.put("success", 0);
		count.put("failure", 0);
		try {
			 records = CsvUtil.readCSV(file);
		}catch(EmployeeDataAccessException e){
			throw new EmployeeServiceException(e);
		}
        
		for (String[] values : records) 
        {
			count.put("total", count.get("total") + 1);
        	Employee employee =new Employee();
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
    	   }catch (NumberFormatException | ParseException | ArrayIndexOutOfBoundsException e) {
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
    			}if (employeeDao.saveEmployee(employee)) {
                    count.put("success", count.get("success") + 1);
                } else {
                    count.put("failure", count.get("failure") + 1);
                }	
    		}catch(EmployeeDataAccessException e) {

                    throw new EmployeeServiceException("Fatal DB error: Connection lost", e);
                }
            }    	
        	
		return count;
	}

	public List<Employee> readAllFromDb() throws EmployeeServiceException
	{
		
		try{
			return employeeDao.selectAllEmployees();
		}
		catch(EmployeeDataAccessException e) {
			throw new EmployeeServiceException(e);	     
		}
	}
	
	public Employee getEmployeeById(int employeeId) throws EmployeeServiceException
	{
		try{
			return employeeDao.findById(employeeId);
		}
		catch(EmployeeDataAccessException e) {
			throw new EmployeeServiceException(e);	     
		}
	}
	
	public int updateEmployee(Employee employee) throws EmployeeServiceException{
		try {
			return employeeDao.updateEmployee(employee);
		}catch(EmployeeDataAccessException e) {
			throw new EmployeeServiceException(e);	     
		}
	}
	
	public int deleteEmployeeById(int employeeId) throws EmployeeServiceException{
		try {
			return employeeDao.deleteEmployee(employeeId);
		}catch(EmployeeDataAccessException e) {
			throw new EmployeeServiceException(e);	     
		}
	}
	public boolean addEmployee(Employee employee) throws EmployeeServiceException{
		try {
			if(employeeDao.doesEmployeeExist(employee.getEmployeeId()))
				throw new EmployeeServiceException("Employee Already Exists !!!");
			return employeeDao.saveEmployee(employee);
		}catch(EmployeeDataAccessException e) {
			throw new EmployeeServiceException(e);	     
		}
	}
	
	
	

}
