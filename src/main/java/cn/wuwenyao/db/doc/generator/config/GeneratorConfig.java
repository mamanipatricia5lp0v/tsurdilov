package cn.wuwenyao.db.doc.generator.config;

import cn.wuwenyao.db.doc.generator.enums.DbType;
import cn.wuwenyao.db.doc.generator.enums.TargetFileType;

/***
 * 生成器配置信息
 * 
 * @author wwy shiqiyue.github.com
 *
 */
public class GeneratorConfig {
	
	/***
	 * 数据库类型
	 */
	private DbType dbtype = DbType.MYSQL;
	
	/***
	 * 生成文件类型
	 */
	private TargetFileType targetFileType = TargetFileType.WORD;
	
	/***
	 * 生成文件目录
	 */
	private String targetFileDir = "";

	/***
	 * 模板文件地址
	 */
	private String templateFilePath = "";
	
	public DbType getDbtype() {
		return dbtype;
	}
	
	public void setDbtype(DbType dbtype) {
		this.dbtype = dbtype;
	}
	
	public TargetFileType getTargetFileType() {
		return targetFileType;
	}
	
	public void setTargetFileType(TargetFileType targetFileType) {
		this.targetFileType = targetFileType;
	}

	public String getTargetFileDir() {
		return targetFileDir;
	}

	public void setTargetFileDir(String targetFileDir) {
		this.targetFileDir = targetFileDir;
	}


	public String getTemplateFilePath() {
		return templateFilePath;
	}

	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}
}
