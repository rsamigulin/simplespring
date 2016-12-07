package itpark.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itpark.exception.MyException;
import itpark.model.Product;
import itpark.repository.ProductRepository;


@Service("productService")
public class ProductServiceImpl implements ProductService {
	private ProductRepository repository;

	@Autowired
	public ProductServiceImpl(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Product> getAll() {
		return repository.getAll();
	}

	@Override
	public List<Product> searchByName(String name) throws MyException {
		return repository.getByName(name);
	}

	@Override
	public void sell(int id, int count) throws MyException {
		repository.sell(id, count);
	}

	@Override
	public Product getById(int id) throws MyException {
		return repository.getById(id);
	}
}
