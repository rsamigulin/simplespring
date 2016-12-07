package itpark.repository;

import java.util.List;

import itpark.exception.MyException;
import itpark.model.Product;


public interface ProductRepository {
	
	public List<Product> getByName(String searchString) throws MyException;
	
	public List<Product> getAll();
	
	public void sell(int id, int count) throws MyException;
	
	public Product getById(int id) throws MyException;
	
	

}
