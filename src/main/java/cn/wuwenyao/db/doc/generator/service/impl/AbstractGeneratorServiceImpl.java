package cn.wuwenyao.db.doc.generator.service.impl;

import cn.wuwenyao.db.doc.generator.dao.DbInfoDao;
import cn.wuwenyao.db.doc.generator.service.GeneratorService;

public abstract class AbstractGeneratorServiceImpl implements GeneratorService {
	
	protected DbInfoDao dbInfoDao;
	
	protected String targetFileDir;
	
	@Override
	public void generateDbDoc() throws Exception {
	}
	
	@Override
	public void setDbInfoDao(DbInfoDao dbInfoDao) {
		this.dbInfoDao = dbInfoDao;
	}
	
	@Override
	public void setTargetFileDir(String targetFileDir) {
		this.targetFileDir = targetFileDir;
	}
	
}