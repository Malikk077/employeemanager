package com.litmus7.EmployeeManager.UI;
import java.util.List;
import com.litmus7.EmployeeManager.Controller.EmployeeManagerController;
import com.litmus7.EmployeeManager.Dto.Employees;
import com.litmus7.EmployeeManager.Dto.Response;



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
		
		Response<List<Employees>> employeesResponse=controller.getAllEmployees();
		if (employeesResponse.getStatusCode()==200) 
		{
			for (Employees emp:employeesResponse.getData())
			{
				System.out.println(emp.toDetailedString());
		    }
		}
		else {
			System.out.println("Message: " + employeesResponse.getErrorMessage());
		}
		
	}

}

