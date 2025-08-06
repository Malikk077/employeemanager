package com.litmus7.employeemanager.exception;

public class EmployeeDataAccessException extends  Exception{

   
    public EmployeeDataAccessException(String message) {
        super(message);
    }

   
    public EmployeeDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }


}
