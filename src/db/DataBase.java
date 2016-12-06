package db;

import java.sql.Connection;

public interface DataBase {

	public void loadDriver();
	public Connection getConnection();
	
}
