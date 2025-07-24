package com.litmus7.EmployeeManager.property;

public class DbConfig {
	private String url;
    private String username;
    private String password;
    
    public DbConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
	
    public static DbConfig getDatabaseConfig() {
        String URL = "jdbc:mysql://localhost:3306/empl";
        String USER = "root";
        String PASS = "#Ssfam123";
        
        return new DbConfig(URL, USER, PASS);
    }

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	

}
