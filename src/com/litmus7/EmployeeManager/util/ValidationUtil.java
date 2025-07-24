package com.litmus7.EmployeeManager.util;

import java.util.regex.Pattern;
import com.litmus7.EmployeeManager.Dto.Employees;

public class ValidationUtil {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_REGEX = Pattern.compile("^\\d{10}$");

    public static boolean isNonEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return isNonEmpty(email) && EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return isNonEmpty(phone) && PHONE_REGEX.matcher(phone).matches();
    }

    public static boolean isValidSalary(Double salary) {
        return salary != null && salary >= 0;
    }

    public static boolean isValidDate(java.util.Date date) {
        return date != null;
    }

    public static boolean validateEmployee(Employees emp) {
        return emp.getEmployeeId() > 0
            && isNonEmpty(emp.getFirstName())
            && isNonEmpty(emp.getLastName())
            && isValidEmail(emp.getEmail())
            && isValidPhone(emp.getPhone())
            && isNonEmpty(emp.getDepartment())
            && isValidSalary(emp.getSalary())
            && isValidDate(emp.getJoinDate());
    }
}
