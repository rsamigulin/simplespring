package itpark;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import itpark.server.Server;

public class Main {

	public static void main(String[] args) {
    	 
    	 ApplicationContext applicationContext =
                 new AnnotationConfigApplicationContext(AppConfig.class);
         Server server =
                 applicationContext.getBean("serverService", Server.class);
         
         server.start();
	}
}
