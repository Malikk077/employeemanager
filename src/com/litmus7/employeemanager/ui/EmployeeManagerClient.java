package com.litmus7.employeemanager.ui;
import java.time.LocalDate;
import java.util.List;
import com.litmus7.employeemanager.controller.EmployeeManagerController;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;



public class EmployeeManagerClient {

	public static void main(String[] args) {
		EmployeeManagerController controller = new EmployeeManagerController();
		
		
		Response<Integer> response = controller.writeDataToDb("employees.csv");//write data to db from a csv file
		if (response.getStatusCode() == 200) {
            System.out.println("All records were inserted succesfully.");
            System.out.println("Rows inserted: "+ response.getData());
        } else if (response.getStatusCode() == 207) {
            System.out.println("Message: " + response.getErrorMessage());
            System.out.println("Inserted Count: " + response.getData());
        } else {
            System.out.println("Error Code : "+response.getStatusCode());
            System.out.println("Message: " + response.getErrorMessage());
        }

		Response<List<Employee>> employeesResponse=controller.getAllEmployees();//get the details of all the employees 
		if (employeesResponse.getStatusCode()==200) 
		{
			for (Employee emp:employeesResponse.getData())
			{
				System.out.println(emp.toDetailedString());
		    }
		}
		else {
			System.out.println("Error Code : "+employeesResponse.getStatusCode());
			System.out.println("Message: " + employeesResponse.getErrorMessage());
		}
		
		Response<Employee> employeeByIdResponse =controller.getEmployeeById(103);//get a single employee by id
		if (employeeByIdResponse.getStatusCode()==200) 
		{
			Employee employee=employeeByIdResponse.getData();
			System.out.println(employee.toDetailedString());
		}else {
		    System.out.println("Error Code : "+employeeByIdResponse.getStatusCode());
		    System.out.println("Message: " + employeeByIdResponse.getErrorMessage());
		}
		
		
		Employee employee =new Employee();//update by ID 
		employee.setEmployeeId(103);
		employee.setFirstName("jake");
		employee.setLastName("black");
		employee.setEmail("jackblack@gmail.com");
		employee.setPhone("94454528288");
		employee.setDepartment("HR");
		employee.setSalary(92155.00);
		employee.setJoinDate(LocalDate.parse("2021-02-12"));
		Response<Integer> updateEmployeeResponse =controller.updateEmployee(employee);
		if (updateEmployeeResponse.getStatusCode()==200) 
		{
			System.out.println("Records updated Successfully, Records Affected :"+updateEmployeeResponse.getData());
			
		}else {
		    System.out.println("Error Code : "+updateEmployeeResponse.getStatusCode() );
		    System.out.println("Message: " + updateEmployeeResponse.getErrorMessage());
		    System.out.println("Records Affected :"+updateEmployeeResponse.getData());
		}

		Response<Integer> deleteByIdResponse =controller.deleteEmployeeById(103);//Delete a record by Id
		if (deleteByIdResponse.getStatusCode()==200) 
			System.out.println("Records Deleted Successfully, Records Affected :"+deleteByIdResponse.getData());
		else {
			System.out.println("Error Code :"+deleteByIdResponse.getStatusCode());
		    System.out.println("Message: " + deleteByIdResponse.getErrorMessage());
		    System.out.println("Records Affected : "+deleteByIdResponse.getData());
		}
		
		Employee employeee =new Employee();//add a single employee 
		employeee.setEmployeeId(103);
		employeee.setFirstName("jake");
		employeee.setLastName("black");
		employeee.setEmail("jackblack@gmail.com");
		employeee.setPhone("94454528288");
		employeee.setDepartment("HR");
		employeee.setSalary(92155.00);
		employeee.setJoinDate(LocalDate.parse("2021-02-12"));
		Response<Integer> addEmployeeResponse =controller.addEmployee(employeee);
		if (addEmployeeResponse.getStatusCode()==200) 
			System.out.println("Record Inserted Succesfully, Row Id : "+addEmployeeResponse.getData());
		else {
			System.out.println("Error Code :"+addEmployeeResponse.getStatusCode());
		    System.out.println("Message: " + addEmployeeResponse.getErrorMessage());
		}
	
		    
	
	
	
	
	}

}
	


