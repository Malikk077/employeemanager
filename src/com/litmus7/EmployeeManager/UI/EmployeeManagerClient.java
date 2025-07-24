package com.litmus7.EmployeeManager.UI;

import java.util.List;

import com.litmus7.EmployeeManager.Controller.EmployeeManagerController;
import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.Dto.Response;
import com.litmus7.EmployeeManager.constant.Constant;

public class EmployeeManagerClient {

	public static void main(String[] args) {
		EmployeeManagerController controller = new EmployeeManagerController();
		
		Response<Integer> result = controller.writeDataToDb("src/com/litmus7/EmployeeManager/Dto/employees.csv");
		if (result.getStatusCode()==200) 
		{
			System.out.println(result.getStatusCode()+"  "+Constant.errorCodeMap.get(result.getStatusCode())+"  No of rows inserted:"+result.getData());
		}
		else {
			System.out.println(result.getStatusCode()+"  "+Constant.errorCodeMap.get(result.getStatusCode())+"  No of rows inserted:"+result.getData());
		}
		
//		Response<List<Employees>> employeeListResponse=controller.getAllEmployees();
//		if (employeeListResponse.getStatusCode()==200) 
//		{
//			for (Employees emp:employeeListResponse.getData())
//			{
//				System.out.println(emp.toDetailedString());
//		    }
//		}
//		else {
//			System.out.println(employeeListResponse.getStatusCode()+"  "+Constant.errorCodeMap.get(employeeListResponse.getStatusCode())+"  "+employeeListResponse);
//		}
		
	}

}
