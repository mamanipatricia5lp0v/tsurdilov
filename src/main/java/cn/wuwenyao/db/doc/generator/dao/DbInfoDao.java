package cn.wuwenyao.db.doc.generator.dao;

import java.util.List;

import cn.wuwenyao.db.doc.generator.entity.TableInfo;

/***
 * 获取数据库信息的dao接口
 * 
 * @author wwy
 *
 */
public interface DbInfoDao {

	/****
	 * 获取数据库名称
	 * 
	 * @return
	 */
	public String databaseName();

	/***
	 * 获取表的信息
	 * 
	 * @return
	 */
	public List<TableInfo> tableInfoList();

}
