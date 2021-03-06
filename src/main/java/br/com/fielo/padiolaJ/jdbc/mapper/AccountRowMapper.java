package br.com.fielo.padiolaJ.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.fielo.padiolaJ.domain.Account;

public class AccountRowMapper implements RowMapper<Account> {
	
		@Override
		public Account mapRow(ResultSet row, int rowNum) throws SQLException {
			Account obj = new Account();
			obj.setId(row.getString("Id"));
			obj.setName(row.getString("Name"));
			obj.setRating(row.getString("Rating"));
			obj.setIndustry(row.getString("Industry"));
			
			
			return obj;
		}


}
