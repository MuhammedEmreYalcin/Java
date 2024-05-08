
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class MySQLConnection {
	public static void main(String[] args) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libraryProject","root","12345");
			System.out.println("Connection to the database successfully");
		} catch(SQLException e) {
			System.out.println("Error while connecting to the database: " + e.getMessage());
		}
	}
}

