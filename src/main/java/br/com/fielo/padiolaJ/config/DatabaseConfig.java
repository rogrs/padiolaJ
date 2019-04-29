package br.com.fielo.padiolaJ.config;


import com.zaxxer.hikari.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import javax.sql.DataSource;

@Configuration
@Profile("heroku")
public class DatabaseConfig {

  @Value("${spring.datasource.url}")
  private String dbUrl;
  
  @Value("${localDatabaseUrl}")
  private String dbUrlLocal;

  @Bean
  public DataSource dataSource() {
      HikariConfig config = new HikariConfig();
      if (null == dbUrl) {
    	  config.setJdbcUrl(dbUrlLocal);  
      } else {
      config.setJdbcUrl(dbUrl);
      }
      return new HikariDataSource(config);
  }
}