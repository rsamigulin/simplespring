package itpark.service;

import java.util.List;
import itpark.exception.MyException;
import itpark.model.Product;


public interface ProductService {
	
	List<Product> getAll();
	
	List<Product> searchByName(String name) throws MyException;
	
	void sell(int id, int count) throws MyException;
	
	Product getById(int id) throws MyException;

}
