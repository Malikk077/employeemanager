package com.litums7.EmployeeManager.exception;

public class EmployeeDataAccessException extends  Exception{

   
    public EmployeeDataAccessException(String message) {
        super(message);
    }

   
    public EmployeeDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }


}
