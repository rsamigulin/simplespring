package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DataBaseImpl implements DataBase{

	public String login;
	public String password;
	public String dbname;
	public String host;
	Connection connection;
	
	private static final String DRIVER = "org.postgresql.Driver"; 
	
//	public void init(){
//		loadDriver();
////		conn = getConnection();
//		try {
//			conn.setAutoCommit(false);
//		} catch (SQLException e) {
//	}
		
		
//		Product catalog;
//	}
	
	private static final Logger logger = Logger.getLogger(DataBaseImpl.class);
	
//	Connection conn;
	
	public DataBaseImpl() {
		String login = "postgres";
		String pass = "1q2w3e4R";
		String dbname = "catalog";
//		String host = "localhost";
		String host = "10.150.116.71";
 		
		DataBase database = new DataBaseImpl(login, password, dbname, host);
		connection = getConnection();
	}
	
	public DataBaseImpl(String login, String password, String dbname, String host) {
		this.login = login;
		this.password = password;
		this.dbname = dbname;
		this.host = host;
//		init();
		loadDriver();
	}
	
	
	public void loadDriver(){
		try {
			Class.forName(DRIVER);
			logger.info(" Драйвер загружен");
		} catch (ClassNotFoundException e) {
			logger.error(" Драйвер не загружен" + e.getMessage());
		}
	}
	
	public Connection getConnection(){
//		loadDriver();
		String jdbcURL = "jdbc:postgresql://" + host + ":5432/" + dbname;
		try {
			logger.info(" Подключение к базе данных " + dbname + "...");
			Connection conn = DriverManager.getConnection(jdbcURL, login , password);
			logger.info("Успешно!");
			return conn;
		} catch (SQLException e) {
			logger.error("Нет подключения к БД " + e.getMessage());
		}
		return null;
	}
}
