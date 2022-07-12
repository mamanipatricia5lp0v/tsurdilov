package cn.wuwenyao.db.doc.generator.service;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.wuwenyao.db.doc.generator.dao.DbInfoDao;
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
	private DbInfoDao dbInfoDao;
	
	/***
	 * 生成数据库文件放置的位置
	 */
	@Value("${application.db.generator.target}")
	private String targetFileName;
	
	/***
	 * 生成数据库文档
	 * 
	 * @throws Exception
	 */
	public void generateDbDoc() throws Exception {
		String databaseName = dbInfoDao.databaseName();
		List<TableInfo> tableInfos = dbInfoDao.tableInfoList();
		// 获取模板
		Template template = FreemarkerUtils.getTemplate("dbTemplate2.ftl");
		File file = new File(targetFileName);
		// 生成目录
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		Map<String, Object> map = new HashMap<>(2);
		map.put("tableInfos", tableInfos);
		map.put("databaseName", databaseName);
		// 根据模板生成文件
		template.process(map, new FileWriter(file));
	}
	
}
