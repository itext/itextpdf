package classroom.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseFill {

	public static void main(String[] args) {
		try {
			Class.forName(Constants.DRIVER);
			Connection connection =
				DriverManager.getConnection(Constants.CONNECTSTRING, Constants.USERNAME, Constants.PASSWORD);
			Statement stmt = connection.createStatement();
			read(stmt, Constants.SCRIPTS + "film_tables.sql");
			read(stmt, Constants.SCRIPTS + "film_title.sql");
			read(stmt, Constants.SCRIPTS + "director_name.sql");
			read(stmt, Constants.SCRIPTS + "film_director.sql");
			read(stmt, Constants.SCRIPTS + "film_category.sql");
			read(stmt, Constants.SCRIPTS + "festival_entry.sql");
			read(stmt, Constants.SCRIPTS + "festival_screening.sql");
			connection.commit();         
			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void read(Statement stmt, String file) throws IOException, SQLException {
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;
		StringBuffer buf = new StringBuffer();
		while (reader.ready()) {
			line = reader.readLine().trim();
			if (!line.startsWith("--")) {
				buf.append(line);
				if (line.endsWith(";")) {
					buf.deleteCharAt(buf.toString().length() - 1);
					System.out.println(buf.toString());
					stmt.execute(buf.toString());
					buf = new StringBuffer();
				}
			}
		}
	}
}
