package java_products;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class DBConnector {
	
	Connection conn = null;
	public static Connection dbConnector() {
		String url = "jdbc:mysql://localhost:3306/products_db?useTimezone=true&serverTimezone=UTC";
		String username = "root";
		String password = "";
		
		System.out.println("Connecting database..."); 
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		/**
		 * For Testing:
		 * System.out.println("Driver loaded!"); 
		 * */
		} catch (ClassNotFoundException e) {
		throw new IllegalStateException("Cannot find the driver in the classpath!",e); }
		
		
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			
			/**
			 * For connection testing:
			 * System.out.println("Connection Successfull!");
			 * JOptionPane.showMessageDialog(null, "Connection Successfull");
			 * */
			return conn;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,e);
			return null;
		}
		

	}

}
