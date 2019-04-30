package br.com.fielo.padiolaJ.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.fielo.padiolaJ.domain.Account;

public class AccountRowMapper implements RowMapper<Account> {
	
		@Override
		public Account mapRow(ResultSet row, int rowNum) throws SQLException {
			Account obj = new Account();
			obj.setId(row.getString("id"));
			obj.setName(row.getString("name"));
			obj.setRating(row.getString("rating"));
			obj.setIndustry(row.getString("industry"));
			
			
			return obj;
		}


}
