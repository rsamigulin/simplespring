package itpark.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import itpark.service.ProductService;


@Component("serverService")
public class Server {
	
//	private static final Logger logger = Logger.getLogger(Server.class);
	private Logger logger = Logger.getLogger(getClass().getName());
	public static Clients clients;
	
	private ProductService productService;
	
	@Autowired
	public Server(ProductService productService){
		this.productService = productService;
	}
	
	public void start() {
	
		ClientThread client = null;
		clients = new Clients();
		
		try(ServerSocket serverSocket = new ServerSocket(8082)){
			logger.info("wait for a client...");
			
			while(true){
				Socket socket = serverSocket.accept();
				client = new ClientThread(socket, productService);
				client.start();
				clients.addUser(client);
				logger.info("new client connected");
			}
			
		} catch (IOException e) {
//			e.printStackTrace();
		} 
	}
}
