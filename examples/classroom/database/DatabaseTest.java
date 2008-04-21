package classroom.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {
	public static void main(String[] args) {
		try {
			Class.forName(Constants.DRIVER);
			Connection connection =
				DriverManager.getConnection(Constants.CONNECTSTRING, Constants.USERNAME, Constants.PASSWORD);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT title FROM film_title ORDER BY title");
			while (rs.next()) {
				System.out.println(rs.getString("title"));
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
