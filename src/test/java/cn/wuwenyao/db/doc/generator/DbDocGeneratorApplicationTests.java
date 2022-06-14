package cn.wuwenyao.db.doc.generator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;

import cn.wuwenyao.db.doc.generator.entity.TableFieldInfo;
import cn.wuwenyao.db.doc.generator.entity.TableInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbDocGeneratorApplicationTests {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void generateDbDoc() {
		List<TableInfo> tableInfos = jdbcTemplate.query("show tables;", new TableInfoRowMapper());
		tableInfos.stream().forEach((tableInfo) -> {
			List<TableFieldInfo> fields = jdbcTemplate.query("describe " + tableInfo.getTableName(),
					new TableFieldInfoRowMapper());
			tableInfo.setFields(fields);
		});
		
		System.out.println(JSON.toJSONString(tableInfos));
	}
	
	public static class TableInfoRowMapper implements RowMapper<TableInfo> {
		
		@Override
		public TableInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			String tableName = rs.getString(1);
			TableInfo tableInfo = new TableInfo();
			tableInfo.setTableName(tableName);
			return tableInfo;
		}
		
	}
	
	public static class TableFieldInfoRowMapper implements RowMapper<TableFieldInfo> {
		
		@Override
		public TableFieldInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TableFieldInfo tableFieldInfo = new TableFieldInfo();
			tableFieldInfo.setDefaultValue(rs.getString("Default"));
			tableFieldInfo.setExtra(rs.getString("Extra"));
			tableFieldInfo.setField(rs.getString("Field"));
			tableFieldInfo.setKey(rs.getString("Key"));
			tableFieldInfo.setNullAble(rs.getString("Null"));
			tableFieldInfo.setType(rs.getString("Type"));
			return tableFieldInfo;
		}
		
	}
	
}
