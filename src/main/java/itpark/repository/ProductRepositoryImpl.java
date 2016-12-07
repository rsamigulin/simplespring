package itpark.repository;

//import static util.sql.TransactionManager.getTxConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import itpark.exception.MyException;
import itpark.model.Product;
import itpark.server.ClientThread;



@Repository("productRepository")
public class ProductRepositoryImpl implements ProductRepository {

	private static final Logger logger = Logger.getLogger(ClientThread.class);
//	private Logger logger = Logger.getLogger(getClass().getName());
	
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Product> getAll(){
		String sql = "SELECT * FROM catalog";
		
		return jdbcTemplate.query(sql, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				Product product = new Product();
				product.setId(rs.getInt("id"));
				product.setName(rs.getString("name"));
				product.setDescription(rs.getString("description"));
				product.setPrice(rs.getDouble("price"));
				product.setQuantity(rs.getInt("quantity"));
				return product;
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Product> getByName(String searchString) throws MyException{
		String sql = "SELECT * FROM catalog WHERE LOWER(name) LIKE ?";
		List<Product> list = jdbcTemplate.query(sql, 
				new Object[] {"%" + searchString + "%"},
				new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				
				Product product = new Product();
				product.setId(rs.getInt("id"));
				product.setName(rs.getString("name"));
				product.setDescription(rs.getString("description"));
				product.setPrice(rs.getDouble("price"));
				product.setQuantity(rs.getInt("quantity"));
				
				return product;
			}
		});
				 System.out.println(list.size());
				 if(list.size() == 0)
						throw new MyException("from repo elements with name  " + searchString + " not exists");
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Product getById(int id) throws MyException{
		
		String sql = "SELECT * FROM catalog WHERE id = ?";
		
		List<Product> list = jdbcTemplate.query(sql, 
				new Object[] {id},
				new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				
				Product product = new Product();
				product.setId(rs.getInt("id"));
				product.setName(rs.getString("name"));
				product.setDescription(rs.getString("description"));
				product.setPrice(rs.getDouble("price"));
				product.setQuantity(rs.getInt("quantity"));
				
				return product;
			}
		});
		
		if(list.size() == 0)
			throw new MyException("elements with id " + id + " not exists");
		
		return list.get(0);
	}
	
	@Override
	public void sell(int id, int countToSell) throws MyException{
		
		Product product = getById(id);
		
		if(product == null){
			logger.info("В БД отсутствует товар с id = " + id);
			throw new MyException("В БД отсутствует товар с id = " + id);
		}
		
		if( countToSell > product.getQuantity()){
				logger.info("Количество для продажи не может превышать количество товара на складе");
				throw new MyException("Количество для продажи не может превышать количество товара на складе");
		}
		
		jdbcTemplate.update("UPDATE catalog SET quantity = ? where id = ?",
				product.getQuantity() - countToSell, id);
	}
}
