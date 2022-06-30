package cn.wuwenyao.db.doc.generator.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.wuwenyao.db.doc.generator.dao.DbInfoDao;
import cn.wuwenyao.db.doc.generator.entity.TableFieldInfo;
import cn.wuwenyao.db.doc.generator.entity.TableInfo;

/***
 * 获取mysql数据库信息
 * 
 * @author wwy
 *
 */
@ConditionalOnProperty(name = "application.db.type", havingValue = "mysql")
@Component
public class MysqlDbInfoDao implements DbInfoDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String databaseName() {
		String databaseName = jdbcTemplate.queryForObject("select database()", String.class);
		return databaseName;
	}

	@Override
	public List<TableInfo> tableInfoList() {
		List<TableInfo> tableInfos = jdbcTemplate.query(
				"select table_name,table_comment from information_schema.tables where table_schema = database()",
				new TableInfoRowMapper());
		tableInfos.stream().forEach((tableInfo) -> {
			Object[] params = new Object[] { tableInfo.getTableName() };
			List<TableFieldInfo> fields = jdbcTemplate.query(
					"select COLUMN_NAME, COLUMN_COMMENT,COLUMN_DEFAULT,IS_NULLABLE,COLUMN_TYPE,COLUMN_KEY,EXTRA from information_schema.columns where table_schema =database() and table_name = ?",
					params, new TableFieldInfoRowMapper());
			tableInfo.setFields(fields);
		});
		return tableInfos;
	}

	public static class TableInfoRowMapper implements RowMapper<TableInfo> {

		@Override
		public TableInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			String tableName = rs.getString(1);
			String remark = rs.getString(2);
			TableInfo tableInfo = new TableInfo();
			tableInfo.setTableRemark(remark);
			tableInfo.setTableName(tableName);
			return tableInfo;
		}

	}

	public static class TableFieldInfoRowMapper implements RowMapper<TableFieldInfo> {

		@Override
		public TableFieldInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			TableFieldInfo tableFieldInfo = new TableFieldInfo();
			tableFieldInfo.setDefaultValue(rs.getString("COLUMN_DEFAULT"));
			tableFieldInfo.setExtra(rs.getString("EXTRA"));
			tableFieldInfo.setField(rs.getString("COLUMN_NAME"));
			tableFieldInfo.setKey(rs.getString("COLUMN_KEY"));
			tableFieldInfo.setNullAble(rs.getString("IS_NULLABLE"));
			tableFieldInfo.setType(rs.getString("COLUMN_TYPE"));
			tableFieldInfo.setRemark(rs.getString("COLUMN_COMMENT"));
			if (tableFieldInfo.getDefaultValue() == null) {
				tableFieldInfo.setDefaultValue("无");
			}
			return tableFieldInfo;
		}

	}

}
