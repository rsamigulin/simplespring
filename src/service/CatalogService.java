package service;

import java.util.List;

import db.Product;

public interface CatalogService {
	
	public List<Product> getAll();
	
	public List<Product> searchByName(String name);
	
	public void sell(int id, int count);
	
	public Product getById(int id);

}
