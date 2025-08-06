package com.litmus7.employeemanager.util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.litmus7.employeemanager.exception.EmployeeDataAccessException;

public class CsvUtil 
{
	public static List<String[]> readCSV(String file) throws EmployeeDataAccessException
    {
        
        List<String[]> data = new ArrayList<>();
       

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);

            String line;
            br.readLine(); // Skip header line
            String header=br.readLine();//one skip line not working

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
	

}
