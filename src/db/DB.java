package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

	private static Connection conn = null;
	
	public static Connection getConnection() {
		if (conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
			return conn;
	}
	
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
		
		Properties props = new Properties();
		
		try(FileInputStream fs = new FileInputStream("./db.properties")) {
			
			props.load(fs);
			
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return props;
	}

	@SuppressWarnings("serial")
	class DbException extends RuntimeException {
		
		public DbException(String msg) {
			super(msg);
		}
	}
}