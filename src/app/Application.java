
package app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import server.ClientThread;
import server.Server;
import service.CatalogService;
import service.CatalogServiceImpl;
import db.DataBase;
import db.DataBaseImpl;
import db.Product;

public class Application {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		CatalogService catalogService = context.getBean("catalogService", CatalogService.class);
		
//		String login = "postgres";
//		String pass = "1q2w3e4R";
//		String dbname = "catalog";
////		String host = "localhost";
//		String host = "10.150.116.71";
// 		
//		DataBase db = new DataBaseImpl(login,pass,dbname,host);
		
//		Catalog catalog = new Catalog(db);
		
//		System.out.println(catalogService.getAll());
		
//		ClientThread ct = context.getBean("clientThread", ClientThread.class);
//		catalogService = ct.getCatalogService();
		Server server = new Server();
		server.start(catalogService);
//		server.start();
	}
}
