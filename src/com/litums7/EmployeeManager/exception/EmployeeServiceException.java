package com.litums7.EmployeeManager.exception;

public class EmployeeServiceException extends  Exception{

   
    public EmployeeServiceException(String message) {
        super(message);
    }

   
    public EmployeeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmployeeServiceException( Throwable cause) {
        super( cause);
    }
}



