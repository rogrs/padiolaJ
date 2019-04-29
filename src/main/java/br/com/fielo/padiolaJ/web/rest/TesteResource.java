package br.com.fielo.padiolaJ.web.rest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TesteResource {
	
	private final Logger log = LoggerFactory.getLogger(TesteResource.class);
	
	@Autowired
    private DataSource dataSource;

	
	
	@GetMapping("/testes")
	public ResponseEntity<List<String>> getAll() { 
		
		
		List<String> lista = new ArrayList<>();
		Statement stmt = null;
		try {
			stmt = dataSource.getConnection().createStatement();
		} catch (SQLException e) {
			log.error("Erro get datasource {} ",e.getMessage(),e);
		}

   
        ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT name FROM account");
		} catch (SQLException e) {
			log.error("Erro get resultset {} ",e.getMessage(),e);
		}
        try {
			while (rs.next()) {
			    System.out.println("Read from DB: " + rs.getString("name"));
			    
			    lista.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			log.error("Erro load {} ",e.getMessage(),e);
		}
        
        return  ResponseEntity.ok().body(lista);
		
	}

}
