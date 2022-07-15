package cn.wuwenyao.db.doc.generator.enums;

import cn.wuwenyao.db.doc.generator.service.impl.WordGeneratorServiceImpl;

/***
 * 生成文件类型
 * 
 * @author wwy shiqiyue.github.com
 *
 */
public enum TargetFileType {
	
	/** word文档 */
	WORD(WordGeneratorServiceImpl.class),
	/** excel文档 */
	EXCEL(String.class),
	/** html文档 */
	HTML(String.class);
	
	private Class generatorServiceImpl;
	
	private TargetFileType(Class generatorServiceImpl) {
		this.generatorServiceImpl = generatorServiceImpl;
	}
	
	public Class getGeneratorServiceImpl() {
		return generatorServiceImpl;
	}
	
}
