package com.litmus7.employeemanager.ui;
import java.util.List;
import com.litmus7.employeemanager.controller.EmployeeManagerController;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;



public class EmployeeManagerClient {

	public static void main(String[] args) {
		EmployeeManagerController controller = new EmployeeManagerController();
		
		Response<Integer> response = controller.writeDataToDb("employees.csv");
		if (response.getStatusCode() == 200) {
            System.out.println("All records were inserted succesfully.");
            System.out.println("Rows inserted: "+ response.getData());
        } else if (response.getStatusCode() == 207) {
            System.out.println("Message: " + response.getErrorMessage());
            System.out.println("Inserted Count: " + response.getData());
        } else {
            System.out.println("Message: " + response.getErrorMessage());
        }
		
		Response<List<Employee>> employeesResponse=controller.getAllEmployees();
		if (employeesResponse.getStatusCode()==200) 
		{
			for (Employee emp:employeesResponse.getData())
			{
				System.out.println(emp.toDetailedString());
		    }
		}
		else {
			System.out.println("Message: " + employeesResponse.getErrorMessage());
		}
		
		Response<Employee> getEmployeeByIdResponse =controller.getEmployeeById(102);
		if (getEmployeeByIdResponse.getStatusCode()==200) 
		{
			Employee employee=getEmployeeByIdResponse.getData();
			System.out.println(employee.toDetailedString());
		}else {
		    System.out.println("Message: " + getEmployeeByIdResponse.getErrorMessage());
		}


		
	}

}

