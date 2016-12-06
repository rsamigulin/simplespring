package service;

import java.io.IOException;
import java.util.List;

import db.CatalogRepository;
import db.Product;

public class CatalogServiceImpl implements CatalogService{
	
	private CatalogRepository repository;
	
//	public CatalogServiceImpl() {
//	}

	/*public CatalogServiceImpl(CatalogRepository repository) {
		this.repository = repository;
	}*/

	
	
	public void setRepository(CatalogRepository repository) {
		this.repository = repository;
	}

	public CatalogRepository getRepository() {
		return repository;
	}

	@Override
	public List<Product> getAll() {
		return repository.getAll();
	}

	@Override
	public List<Product> searchByName(String name) {
		return repository.getByName(name);
	}

	@Override
	public void sell(int id, int count){
		try {
			repository.sell(id, count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Product getById(int id) {
		return repository.getById(id);
	}
}
