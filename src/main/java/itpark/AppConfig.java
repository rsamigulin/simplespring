package itpark;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan(basePackages = "itpark")
public class AppConfig {
	
	@Bean(name = "dataSource")
    public DataSource getDataSource() {
        
		DriverManagerDataSource driver = new DriverManagerDataSource();
		driver.setDriverClassName("org.postgresql.Driver");
		driver.setUrl("jdbc:postgresql://localhost:5432/simplesite");
	    driver.setUsername("postgres");
	    driver.setPassword("postgres");
		return driver;
    }
}
