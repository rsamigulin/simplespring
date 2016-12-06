package db;

public class Product {
	
	String name;
	String description;
	double price;
	int quantity;
	
	public Product(String name, String description, double price, int quantity) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", description=" + description + ", price=" + price + ", quantity=" + quantity
				+ "]";
	}
	
	public String toAnotherString() {
		return  name + ", " + description + ", цена: " + price + ", " + quantity + " шт.";
	}
}
