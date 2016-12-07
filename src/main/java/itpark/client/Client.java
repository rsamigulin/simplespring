package itpark.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;
//import java.util.logging.Logger;

public class Client {

	private static Logger logger = Logger.getLogger(Client.class);
//	private static Logger logger = Logger.getLogger(getClass().getName());
	private static String username;
	
	public static void main(String[] args) {
		
		try(BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))){
				
			try(Socket socket = new Socket("localhost", 8082)){
				
				logger.info("client start");
			
						try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
								){
							Thread t = new Thread(new Runnable() {
								
								@Override
								public void run() {
									try{
										while(true){
											String s = in.readLine();
//											logger.info(s);
											System.out.println(s);
										}
									} catch (IOException e) {
//										e.printStackTrace();
									}
								}
							});
								
							t.start();
							
							while(true){
								String userMessage = consoleReader.readLine().trim();
								
								if("exit".equals(userMessage)){
									out.write("exit"); 
									out.newLine();
									out.flush();
									socket.close();
								}
								out.write(userMessage); 
								out.newLine();
								out.flush();
							}	
						} catch (IOException ioe) {
//							ioe.printStackTrace();
							socket.close();
						}
						
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} 
		
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} 
	}
}


