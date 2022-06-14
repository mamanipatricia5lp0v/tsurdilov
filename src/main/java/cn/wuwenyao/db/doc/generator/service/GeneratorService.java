package cn.wuwenyao.db.doc.generator.service;

import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import cn.wuwenyao.db.doc.generator.entity.TableFieldInfo;
import cn.wuwenyao.db.doc.generator.entity.TableInfo;
import cn.wuwenyao.db.doc.generator.utils.FreemarkerUtils;
import freemarker.template.Template;

/***
 * 生成文档服务
 * 
 * @author wwy
 *
 */
@Service
public class GeneratorService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${application.db.generator.target}")
	private String targetFileName;
	
	public void generateDbDoc() throws Exception {
		String databaseName = jdbcTemplate.queryForObject("select database()", String.class);
		
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
		Template template = FreemarkerUtils.getTemplate("dbTemplate2.ftl");
		File file = new File(targetFileName);
		if (!file.getParentFile().exists()) { // 判断有没有父路径，就是判断文件整个路径是否存在
			file.getParentFile().mkdirs(); // 不存在就全部创建
		}
		Map<String, Object> map = new HashMap<>();
		map.put("tableInfos", tableInfos);
		map.put("databaseName", databaseName);
		template.process(map, new FileWriter(file));
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
