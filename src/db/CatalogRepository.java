package db;

import java.util.List;

public interface CatalogRepository {
	
	public List<Product> getByName(String searchString);
	
	public List<Product> getAll();
	
	public void sell(int id, int count) throws Exception;
	
	public Product getById(int id);
	
	

}
