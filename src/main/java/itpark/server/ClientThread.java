package itpark.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
//import java.util.concurrent.CopyOnWriteArraySet;
//import java.util.logging.Logger;

import itpark.exception.MyException;
import itpark.model.Product;
import org.apache.log4j.Logger;
import itpark.service.ProductService;

public class ClientThread extends Thread {
		private static final Logger logger = Logger.getLogger(ClientThread.class);
//		private Logger logger = Logger.getLogger(getClass().getName());
		private Socket socket;
		private BufferedReader in;
		private BufferedWriter out;
		private ProductService productService;
		String[] commands;
		String inMessage;
		
		public ClientThread(Socket socket, ProductService productService) throws IOException {
			this.socket = socket;
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			this.productService = productService;
		}
		
		@Override
		public void run() {
			try{
				while(true){
					inMessage = in.readLine().trim();
					if(inMessage == null){
						throw new NullPointerException();
					}
					commands = inMessage.trim().split("[\\s]+");
					while(!isValidCommand(commands[0])){
						Server.clients.sendPersonalMessage(socket, "Server: wrong command! ");
						inMessage = in.readLine().trim();
						commands = inMessage.trim().split("[\\s]+");
					}
					
					switchCommand(commands[0]);
					logger.info(inMessage);
					Server.clients.sendMessage(socket, inMessage);
				}
			} catch (IOException e) {
			} catch (MyException e) {
				Server.clients.sendPersonalMessage(socket, "Server: " + e.getMessage());
			} catch (NullPointerException npe){		
			} finally {
					
				close();
			}
		}

		public void switchCommand(String command) throws IOException, MyException {
			switch(command){
				case "show": 
					getAll();
					break;
				case "search":
					searchByName();
					break;
				case "sell":
					sell();;
					break;
				case "exit":
					exit();
					break;
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
			System.out.println(productService.getAll());
			return productService.getAll();
		}
		
		public List<Product> getProductsByName(String searchString) throws MyException{
			return productService.searchByName(searchString);
		}
		
		public void sellProduct(int id, int count) throws MyException{
			productService.sell(id, count);
		}
		
		public Product getProductById(int id) throws MyException{
			return productService.getById(id);
		}
		
		public String getStringFrom(List<Product> list){
			StringBuilder sb = new StringBuilder();
			
			for(Product product : list){
				sb.append("[ ");
				sb.append(product.toAnotherString());
				sb.append("]");
				sb.append(",\n ");
			}
			return sb.toString();
		}
		
		public void searchByName() throws IOException, MyException{
					while(commands.length == 1){
						Server.clients.sendPersonalMessage(socket, "Server: incomplete command! ");
						readLine();
					}
					while(commands.length > 2){
						Server.clients.sendPersonalMessage(socket, "Server: wrong command! ");
						readLine();
					}	
					if(commands.length == 2){
						String searchName = commands[1].trim();
						try{
							Server.clients.sendPersonalMessage(socket, 
									"Server: records include '" + searchName + "' :"
											+ getStringFrom(getProductsByName(searchName)));
							} catch (MyException e){
								Server.clients.sendPersonalMessage(socket, 
										"elements contains name " + searchName + " not exists");
							}
					}	
		}
		
		public void getAll() throws IOException{
					while(commands.length == 1){
						Server.clients.sendPersonalMessage(socket, "Server: incomplete command! ");
						readLine();
					}
					while(commands.length > 2){
						Server.clients.sendPersonalMessage(socket, "Server: wrong command! ");
						readLine();
					}	
					if(commands.length == 2){
						while(! "all".equals(commands[1])){
							Server.clients.sendPersonalMessage(socket, "Server: wrong command! ");
							readLine();
						} 
						Server.clients.sendPersonalMessage(socket, "Server: all records");
						Server.clients.sendPersonalMessage(socket, "Server: " + getStringFrom(getAllProducts()));
					}	
		}
		
		public void sell() throws MyException, IOException{
				while(commands.length < 3){
					Server.clients.sendPersonalMessage(socket, "Server: incomplete command! ");
					readLine();
				}
				while(commands.length > 3){
					Server.clients.sendPersonalMessage(socket, "Server: wrong command! ");
					readLine();
				}	
				if(commands.length == 3){
					String idStr = commands[1].trim();
					String countStr = commands[2].trim();
					try{
						int id = Integer.parseInt(idStr);
						int count = Integer.parseInt(countStr);
						sellProduct(id, count);
						Server.clients.sendPersonalMessage(socket, 
								"Server: sell " + count + " product  with id = " + id + " OK !");
					} catch (NumberFormatException nfe){
						Server.clients.sendPersonalMessage(socket, "Server: NumberFormatException " + nfe.getMessage());
					} catch (MyException e) {
						Server.clients.sendPersonalMessage(socket, "Server: MyException " + e.getMessage());
						e.printStackTrace();
					} catch (Exception e) {
						Server.clients.sendPersonalMessage(socket, "Server: Exception " + e.getMessage());
					}
				}	
		}
		
		public void exit() throws IOException{
				while(commands.length > 1){
					Server.clients.sendPersonalMessage(socket, "Server: wrong command! ");
					readLine();
				}
				Server.clients.removeUser(this);
				throw new IOException();
		}
		
		public void setProductService(ProductService productService){
			this.productService = productService;
		}

		public ProductService getProductService() {
			return productService;
		}
		
		boolean isValidCommand(String value){
			return ("show".equals(value) || "search".equals(value)
					|| "sell".equals(value) || "exit".equals(value));
			
		}
		public void readLine() throws IOException{
			inMessage = in.readLine();
			commands = inMessage.trim().split("[\\s]+");
		}
}
