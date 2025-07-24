package com.litmus7.EmployeeManager.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.litmus7.EmployeeManager.util.ValidationUtil;
import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.dao.EmployeeDao;

public class EmployeeService {
	public  void writeToDb(String[] values) 
	{
		
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
		catch(Exception e)
		{
			e.printStackTrace();
			return;
			
		}
		if (!ValidationUtil.validateEmployee(employee)) return;
		
		if (EmployeeDao.doesEmployeeExist(employee.getEmployeeId())) return ;
		
		EmployeeDao.storeInDB(employee) ;
	 }

	public List<Employees> readAllFromDb() 
	{
		List<Employees> employeeList =new ArrayList<>();
		employeeList=EmployeeDao.selectAllEmployees();
		return employeeList;
	}

}
