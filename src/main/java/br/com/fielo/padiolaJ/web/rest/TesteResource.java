package br.com.fielo.padiolaJ.web.rest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fielo.padiolaJ.domain.Account;

@RestController
@RequestMapping("/api")
public class TesteResource {

	private final Logger log = LoggerFactory.getLogger(TesteResource.class);
	
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public TesteResource(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@GetMapping("/testes")
	public ResponseEntity<List<Account>> getAll() {

		List<Account> lista =  getAllLines("SELECT name FROM salesforce.account");
		

		return ResponseEntity.ok().body(lista);

	}
	
	
	
	private List<Account> getAllLines(String sql) {

		log.info("Executando sql ={}",sql);
		RowMapper<Account> rowMapper = new AccountRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

}
