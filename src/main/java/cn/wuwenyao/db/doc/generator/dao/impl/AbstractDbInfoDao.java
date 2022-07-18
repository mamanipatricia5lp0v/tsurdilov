package cn.wuwenyao.db.doc.generator.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.wuwenyao.db.doc.generator.dao.DbInfoDao;
import cn.wuwenyao.db.doc.generator.entity.TableInfo;

public abstract class AbstractDbInfoDao implements DbInfoDao {
	
	protected JdbcTemplate jdbcTemplate;
	
	@Override
	public String databaseName() {
		return null;
	}
	
	@Override
	public List<TableInfo> tableInfoList() {
		return null;
	}
	
	@Override
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
