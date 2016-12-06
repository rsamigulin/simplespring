package db;

//import static util.sql.TransactionManager.getTxConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
//import java.util.List;
import java.util.List;

import org.apache.log4j.Logger;




public class  CatalogRepositoryImpl implements CatalogRepository {

	DataBaseImpl database;
	Connection connection;
	private static final Logger logger = Logger.getLogger(CatalogRepositoryImpl.class);
	
	public CatalogRepositoryImpl(){
		
		String login = "postgres";
		String password = "1q2w3e4R";
		String dbname = "simplesite";
//		String host = "localhost";
		String host = "10.150.116.71";
 		
		DataBase database = new DataBaseImpl(login, password, dbname, host);
		
//		database = new DataBaseImpl();
		connection = database.getConnection();
	}
	
	public CatalogRepositoryImpl(DataBaseImpl database){
		this.database = database;
		/* Connection*/ connection = database.getConnection();
		
	}
	
	public DataBaseImpl getDatabase(){
		return database;
	}
//	public Catalog(String login, String password, String dbname, String host) {
//		super(login, password, dbname, host);
//	}

	
//	Connection conn;
	
	@Override
	public List<Product> getAll(){
		String sql = "SELECT * FROM catalog";
		ArrayList<Product> catalog = new ArrayList<>();
			
		try (Statement st = connection.createStatement()){
				connection.setAutoCommit(false);
				st.execute(sql);
				ResultSet rs = st.getResultSet();
				
				while(rs.next()){
					String name = rs.getString("name");
					String description = rs.getString("description");
					double price = rs.getDouble("price");
					int quantity = rs.getInt("quantity");
					Product product = new Product(name, description, price, quantity);
					catalog.add(product);
				}
				
		} catch (SQLException e) {
			logger.error(e.getMessage());
			close();
		}
		
		return catalog;
	}
	
	@Override
	public List<Product> getByName(String searchString){
		String sql = "SELECT * FROM catalog WHERE LOWER(name) LIKE ?";
		
		ArrayList<Product> catalog = new ArrayList<>();
//		try(Connection connection = database.getConnection()){
			try (PreparedStatement pst = connection.prepareStatement(sql)){
				connection.setAutoCommit(false);
				pst.setString(1, "%" + searchString.toLowerCase() + "%");
				ResultSet rs = pst.executeQuery();
				
				while(rs.next()){
					String name = rs.getString("name");
					String description = rs.getString("description");
					double price = rs.getDouble("price");
					int quantity = rs.getInt("quantity");
					Product product = new Product(name, description, price, quantity);
					catalog.add(product);
				}
				
			} catch (SQLException e) {
				logger.error(e.getMessage());
				close();
			}
		
		return catalog;
	}
	
	@Override
	public void sell(int id, int countToSell) throws Exception{
		
		Product product = getById(id);
		
		if(product != null){
			if( countToSell > product.getQuantity()){
				logger.info("РєРѕР»РёС‡РµСЃС‚РІРѕ С‚РѕРІР°СЂР° РЅР° СЃРєР»Р°РґРµ РјРµРЅСЊС€Рµ С‡РµРј СѓРєР°Р·Р°РЅРѕ");
				throw new Exception("РєРѕР»РёС‡РµСЃС‚РІРѕ С‚РѕРІР°СЂР° РЅР° СЃРєР»Р°РґРµ РјРµРЅСЊС€Рµ С‡РµРј СѓРєР°Р·Р°РЅРѕ");
			}
		}
		
		String sql = "UPDATE catalog SET quantity = ? where id = ?";
		
		try (PreparedStatement pst = connection.prepareStatement(sql)){
			connection.setAutoCommit(false);
			
			pst.setInt(1, (product.getQuantity() - countToSell));
			pst.setInt(2, id);
			pst.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			close();
		}
	}
	
	@Override
	public Product getById(int id){
		
		String sql = "SELECT * FROM catalog WHERE id = ?";
		Product product = null;
//		ArrayList<Product> catalog = new ArrayList<>();
		
		try (PreparedStatement pst = connection.prepareStatement(sql)){
			connection.setAutoCommit(false);
			
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			
			if( ! rs.next()){
				logger.info("С‚РѕРІР°СЂ РїРѕ id " + id + " РЅРµ РЅР°Р№РґРµРЅ");
				return null;
			}	
			String name = rs.getString("name");
			String description = rs.getString("description");
			double price = rs.getDouble("price");
			int quantity = rs.getInt("quantity");
			product = new Product(name, description, price, quantity);
			return product;
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			close();
		}
		return product;
	}
	
	public void close(){
		try {
			if(connection != null)
				connection.rollback();
		} catch (SQLException sqle) {
			logger.error(sqle.getMessage() + sqle.getStackTrace() );
		}
	}
}
