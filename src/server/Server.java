package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.CatalogService;
import db.DataBase;

public class Server {
	
	private static final Logger logger = Logger.getLogger(Server.class);
	
	public static Clients clients;
	private CatalogService catalogService;
	
	public DataBase db;
	
	public void start(CatalogService catalogService) {
	
//	public void start() {
		ClientThread client = null;
		clients = new Clients();
		
		try(ServerSocket serverSocket = new ServerSocket(8082)){
			logger.info("wait for a client...");
			
			while(true){
				Socket socket = serverSocket.accept();
//				client = new ClientThread(socket, db);
				client = new ClientThread(socket,catalogService);
//				client = new ClientThread(socket);
				client.start();
				clients.addUser(client);
				logger.info("new client connected");
			}
			
		} catch (IOException e) {
//			e.printStackTrace();
		} 
	}
	

}
