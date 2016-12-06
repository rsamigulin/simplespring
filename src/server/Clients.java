package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import db.Product;

public class Clients {
	
	private CopyOnWriteArrayList<ClientThread> list = new CopyOnWriteArrayList<ClientThread>();
	private CopyOnWriteArraySet<String> names = new CopyOnWriteArraySet<String>();
	
	public void addUser(ClientThread client){
		list.add(client);
	}
	
	public void sendMessage(Socket socket, String message) {
		Iterator<ClientThread> iter = list.iterator();
		while(iter.hasNext()){
			ClientThread elem = iter.next();
			if(!((elem.getSocket()).equals(socket))){
				elem.sendMsg(message);
			}	
		}
	}
	
	public void sendPersonalMessage(Socket socket, List<Product> listMessage){
		Iterator<ClientThread> iter = list.iterator();
		while(iter.hasNext()){
			ClientThread elem = iter.next();
			if(((elem.getSocket()).equals(socket))){
				
				for(Product message : listMessage){
					elem.sendMsg(message + ";/");
				}
			}	
		}
	}
	
	public void sendPersonalMessage(Socket socket, String message) {
		Iterator<ClientThread> iter = list.iterator();
		while(iter.hasNext()){
			ClientThread elem = iter.next();
			if(((elem.getSocket()).equals(socket))){
				elem.sendMsg(message);
			}	
		}
	}
	
	public int getSize(){
		return list.size();
	}
	
	public void removeUser(ClientThread client){
		list.remove(client);
	}

	public CopyOnWriteArrayList<ClientThread> getClientsList(){
		return list;
	}
	
	public CopyOnWriteArraySet<String> getNames(){
		
		return names;
	}
}
