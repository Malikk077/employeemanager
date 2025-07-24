package com.litmus7.EmployeeManager.UI;

import java.util.List;

import com.litmus7.EmployeeManager.Controller.EmployeeManagerController;
import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.Dto.Response;

public class EmployeeManagerClient {

	public static void main(String[] args) {
		EmployeeManagerController controller = new EmployeeManagerController();
		
//		Response<Integer> result = controller.writeDataToDb("src/com/litmus7/EmployeeManager/Dto/employees.csv");
//		if (result.getStatusCode()==200) 
//		{
//			System.out.println(result.getErrorMessage()+"  "+result.getData());
//		}
//		else {
//			System.out.println(result.getStatusCode()+result.getErrorMessage());
//		}
		
		Response<List<Employees>> employeeListResponse=controller.getAllEmployees();
		if (employeeListResponse.getStatusCode()==200) 
		{
			for (Employees emp:employeeListResponse.getData())
			{
				System.out.println(emp.toDetailedString());
		    }
		}
		else {
			System.out.println(employeeListResponse.getStatusCode()+employeeListResponse.getErrorMessage()+employeeListResponse);
		}
		
	}

}
