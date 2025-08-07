package com.litmus7.employeemanager.ui;
import java.time.LocalDate;
import java.util.Arrays;
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
		
		
		Employee employee1 = new Employee();
		employee1.setEmployeeId(104);
		employee1.setFirstName("Anna");
		employee1.setLastName("Smith");
		employee1.setEmail("anna.smith@example.com");
		employee1.setPhone("9123456780");
		employee1.setDepartment("Finance");
		employee1.setSalary(85000.00);
		employee1.setJoinDate(LocalDate.parse("2020-06-15"));

		Employee employee2 = new Employee();
		employee2.setEmployeeId(105);
		employee2.setFirstName("Robert");
		employee2.setLastName("Brown");
		employee2.setEmail("robert.brown@example.com");
		employee2.setPhone("9876543210");
		employee2.setDepartment("IT");
		employee2.setSalary(95000.00);
		employee2.setJoinDate(LocalDate.parse("2019-11-10"));

		Employee employee3 = new Employee();
		employee3.setEmployeeId(106);
		employee3.setFirstName("Emily");
		employee3.setLastName("Davis");
		employee3.setEmail("emily.davis@example.com");
		employee3.setPhone("9898989898");
		employee3.setDepartment("Marketing");
		employee3.setSalary(72000.00);
		employee3.setJoinDate(LocalDate.parse("2022-01-01"));

		Employee employee4 = new Employee();
		employee4.setEmployeeId(107);
		employee4.setFirstName("David");
		employee4.setLastName("Wilson");
		employee4.setEmail("david.wilson@example.com");
		employee4.setPhone("9111223344");
		employee4.setDepartment("HR");
		employee4.setSalary(80000.00);
		employee4.setJoinDate(LocalDate.parse("2021-09-20"));

		Employee employee5 = new Employee();
		employee5.setEmployeeId(108);
		employee5.setFirstName("Sophia");
		employee5.setLastName("Miller");
		employee5.setEmail("sophia.miller@example.com");
		employee5.setPhone("9445566778");
		employee5.setDepartment("Operations");
		employee5.setSalary(89000.00);
		employee5.setJoinDate(LocalDate.parse("2023-04-11"));
		
		List<Employee> employees = Arrays.asList(employee1, employee2,employee3,employee4,employee5);//batch insert a List of employee
		Response<Integer> batchInsertResponse = controller.batchInsertEmployees(employees);
		if (batchInsertResponse.getStatusCode() == 200 || batchInsertResponse.getStatusCode() == 207)
			System.out.println(batchInsertResponse.getErrorMessage()+batchInsertResponse.getData());
		else {
			System.out.println("Error Code :"+batchInsertResponse.getStatusCode());
		    System.out.println("Message: " + batchInsertResponse.getErrorMessage());		
		}
		
		List<Integer> employeeIds = Arrays.asList(104,105,106,107);//updating department of employees using commit and rollback ,full update or nothing
		Response<Integer> transferEmployeesToDepartmentResponse = controller.transferEmployeesToDepartment(employeeIds,"HR");
		if (transferEmployeesToDepartmentResponse.getStatusCode() == 200 )
			System.out.println(transferEmployeesToDepartmentResponse.getErrorMessage());
		else {
			System.out.println("Error Code :"+transferEmployeesToDepartmentResponse.getStatusCode());
		    System.out.println("Message: " + transferEmployeesToDepartmentResponse.getErrorMessage());		
		}
		
	
		
		
		
		
		
	}

}
	


