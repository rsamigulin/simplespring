package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
//import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.log4j.Logger;
import service.CatalogService;
import db.Product;

public class ClientThread extends Thread {

		private static final Logger logger = Logger.getLogger(ClientThread.class);
		private Socket socket;
		private BufferedReader in;
		private BufferedWriter out;
//		private String username;
		private CatalogService catalogService;
		
//		public ClientThread(Socket socket, DataBase database) throws IOException {
		public ClientThread(Socket socket, CatalogService catalogService) throws IOException {
//		public ClientThread(Socket socket) throws IOException {
			this.socket = socket;
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			this.catalogService = catalogService;
		}
		
//		public ClientThread() {
////			this(socket);
//		}
		
		@Override
		public void run() {
//			CopyOnWriteArraySet<String> userList =  Server.clients.getNames();
			
			try{
				while(true){
					String inMessage = in.readLine();
					if(inMessage == null){
						throw new IOException();
					}
					
					getAll(inMessage);
					searchByName(inMessage);
					sell(inMessage);
					exit(inMessage);
					
					logger.info(inMessage);
					Server.clients.sendMessage(socket, inMessage);
				}
				
			} catch (IOException e) {
			} catch (NullPointerException npe){		
			} finally {
					
				close();
			}
		}
	
		public void sendMsg(String message){
			try {
				out.write(message);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public Socket getSocket(){
			return socket;
		}
		
		public void close(){
			if (socket != null){
				try {
					Server.clients.removeUser(this);
					socket.close();
					logger.info("client disconnect");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			close(out);
			close(in);
		}
		
		public boolean isEmptyUsername(String name){
			if(name == null || name.trim().isEmpty()){
				return true;
			} 
			return false;
		}
		
		public void close(Closeable closeable){
			if(closeable != null){
				try {
					closeable.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public List<Product> getAllProducts(){
			return catalogService.getAll();
		}
		
		public List<Product> getProductsByName(String searchString){
			return catalogService.searchByName(searchString);
		}
		
		public void sellProduct(int id, int count) throws Exception{
			catalogService.sell(id, count);
		}
		
		public Product getProductById(int id){
			return catalogService.getById(id);
		}
		
		public String getStringFrom(List<Product> list){
			StringBuilder sb = new StringBuilder();
			for(Product product : list){
				sb.append(product.toAnotherString());
				sb.append(";");
			}
			return sb.toString();
		}
		
		public void searchByName(String command/*, BufferedReader in*/) throws IOException{
			if(command.startsWith("search ")){
				String[] commands = command.split(" ");
				if(commands.length > 2 || commands.length < 2){
					Server.clients.sendPersonalMessage(socket, "Server: incomplete command!");
					command = in.readLine();
				}
				
				if(commands.length == 2){
					String searchName = commands[1];
					Server.clients.sendPersonalMessage(socket, 
							"Server: records include '" + searchName + "' :"
									+ getStringFrom(getProductsByName(searchName)));
//					System.out.println(getAllProducts());
				}
			}
		}
		
		public void getAll(String command) throws IOException{
			if(command.startsWith("show ")){
				String[] commands = command.split(" ");
				if(commands.length > 2 || commands.length < 2){
					Server.clients.sendPersonalMessage(socket, "Server: incomplete command!");
					command = in.readLine();
				}
				
				if(commands.length == 2 && "all".equals(commands[1])){
					Server.clients.sendPersonalMessage(socket, "Server: all records");
					Server.clients.sendPersonalMessage(socket, "Server: " + getStringFrom(getAllProducts()));
//					System.out.println(getAllProducts());
				}
			}
		}
		
		public void sell(String command/*, BufferedReader in*/) throws IOException{
			if(command.startsWith("sell ")){
				String[] commands = command.split(" ");
				if(commands.length > 3 || commands.length < 3){
					Server.clients.sendPersonalMessage(socket, "Server: incomplete command!");
					command = in.readLine();
				}
				
				if(commands.length == 3){
					String idStr = commands[1];
					String countStr = commands[2];
					try{
						int id = Integer.parseInt(idStr);
						int count = Integer.parseInt(countStr);
						
						sellProduct(id, count);
					}  catch (NumberFormatException nfe){
						Server.clients.sendPersonalMessage(socket, "Server: " + nfe.getMessage());
					} catch (Exception e) {
						Server.clients.sendPersonalMessage(socket, "Server: " + e.getMessage());
						e.printStackTrace();
					}
					
					Server.clients.sendPersonalMessage(socket, "Server: sell ");
//					Server.clients.sendPersonalMessage(socket, "Server: all");
//					System.out.println(getAllProducts());
				}
			}
		}
		
		public void exit(String command) throws IOException{
			if("exit".equals(command.trim())){
//				Server.clients.sendPersonalMessage(socket, "Server: goodbye, " + username + "!");
//				Server.clients.getNames().remove(username);
				Server.clients.removeUser(this);
				
				throw new IOException();
			}
		}
		
		public void setCatalogService(CatalogService catalogService){
			this.catalogService = catalogService;
		}

		public CatalogService getCatalogService() {
			return catalogService;
		}
}
